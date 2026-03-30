package nl.biblioblabla.pro.repository;

import nl.biblioblabla.pro.model.Lening;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class LeningenRepository {

    private final JdbcTemplate jdbcTemplate;

    public LeningenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    private final RowMapper<Lening> loanRowMapper = (rs, rowNum) -> {


        Lening loan = new Lening();

        // 2. Koppel de simpele nummers (ID's) uit de database-kolommen aan het Java object
        loan.setId(rs.getInt("id"));
        loan.setGebruikerId(rs.getInt("gebruiker_id"));
        loan.setBoekId(rs.getInt("boek_id"));

        // 3. DATUM VERTALING:
        // Database 'DATE' moet worden omgezet naar Java 'LocalDate'.
        // We checken eerst op 'null' om te voorkomen dat de app crasht als een datum leeg is.
        if (rs.getDate("uitleendatum") != null) {
            loan.setUitleendatum(rs.getDate("uitleendatum").toLocalDate());
        }
        if (rs.getDate("inleverdatum") != null) {
            loan.setInleverdatum(rs.getDate("inleverdatum").toLocalDate());
        }

        // 4. BEOORDELING (Speciale check):
        // rs.getInt geeft altijd een 0 terug als de kolom leeg (NULL) is in de database.
        // Omdat we in Java echt 'null' willen zien (en niet 0), gebruiken we rs.wasNull().
        int beoordeling = rs.getInt("beoordeling");
        loan.setBeoordeling(rs.wasNull() ? null : beoordeling);

        // 5. STATUS:
        // De database kolom 'is_ingeleverd' (0 of 1) wordt hier een Java 'boolean' (true of false).
        loan.setIngeleverd(rs.getBoolean("is_ingeleverd"));

        // 6. EXTRA GEGEVENS (De JOIN resultaten):
        // Omdat de SQL-query ook in de tabellen 'boeken' en 'auteurs' kijkt (via de JOIN),
        // kunnen we hier ook de titel, het genre, de rating en de auteursnaam ophalen.
        loan.setTitel(rs.getString("titel"));
        loan.setGenre(rs.getString("genre"));
        loan.setAvgRating(rs.getDouble("gemiddelde_beoordeling"));
        loan.setAuteurNaam(rs.getString("auteur_naam"));

        // 7. Geef het volledig ingevulde object terug aan de lijst
        return loan;
    };


    private final String JOIN_QUERY =
            "SELECT l.*, b.titel, b.genre, b.gemiddelde_beoordeling, a.naam AS auteur_naam " +
                    "FROM leningen l " +
                    "JOIN boeken b ON l.boek_id = b.id " +
                    "JOIN auteurs a ON b.auteur_id = a.id ";

    public int save(Lening loan) {
        String sql = "INSERT INTO leningen (gebruiker_id, boek_id, uitleendatum, is_ingeleverd) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, loan.getGebruikerId(), loan.getBoekId(), loan.getUitleendatum(), loan.isIngeleverd());
    }

    public List<Lening> findByGebruikerId(int gebruikerId) {
        String sql = JOIN_QUERY+ "WHERE l.gebruiker_id = ?";
        return jdbcTemplate.query(sql, loanRowMapper, gebruikerId);
    }

    public List<Lening> filterOpTitelOfAuteur(String query) {
        String sql = JOIN_QUERY + "WHERE b.titel LIKE ? OR a.naam LIKE ?";
        String fuzzy = "%" + query + "%";
        return jdbcTemplate.query(sql, loanRowMapper, fuzzy, fuzzy);
    }

    public List<Lening> filterOpGenre(String genre) {
        String sql = JOIN_QUERY+ "WHERE b.genre = ?";
        return jdbcTemplate.query(sql, loanRowMapper, genre);
    }

    public List<Lening> filterOpDatumRange(LocalDate start, LocalDate eind) {
        String sql = JOIN_QUERY + "WHERE l.uitleendatum BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, loanRowMapper, start, eind);
    }
}