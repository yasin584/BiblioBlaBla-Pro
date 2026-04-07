package nl.biblioblabla.pro.repository;

import nl.biblioblabla.pro.model.Lening;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LeningenRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor injectie van JdbcTemplate
    public LeningenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Lening> loanRowMapper = (rs, rowNum) -> {
        Lening loan = new Lening();

        // Basisvelden
        loan.setId(rs.getInt("id"));
        loan.setGebruikerId(rs.getInt("gebruiker_id"));
        loan.setBoekId(rs.getInt("boek_id"));

        // Uitleen- en inleverdatum omzetten naar LocalDate
        if (rs.getDate("uitleendatum") != null) {
            loan.setUitleendatum(rs.getDate("uitleendatum").toLocalDate());
        }
        if (rs.getDate("inleverdatum") != null) {
            loan.setInleverdatum(rs.getDate("inleverdatum").toLocalDate());
        }

        // Beoordeling kan null zijn
        int beoordeling = rs.getInt("beoordeling");
        loan.setBeoordeling(rs.wasNull() ? null : beoordeling);

        // Boolean veld
        loan.setIngeleverd(rs.getBoolean("is_ingeleverd"));

        // Extra info uit JOIN
        loan.setTitel(rs.getString("titel"));
        loan.setGenre(rs.getString("genre"));
        loan.setAvgRating(rs.getDouble("gemiddelde_beoordeling"));
        loan.setAuteurNaam(rs.getString("auteur_naam"));

        return loan;  // RowMapper retourneert een Lening object
    };

    private final String JOIN_QUERY =
            "SELECT l.*, b.titel, b.genre, b.gemiddelde_beoordeling, a.naam AS auteur_naam " +
                    "FROM leningen l " +
                    "LEFT JOIN boeken b ON l.boek_id = b.id " +
                    "LEFT JOIN auteurs a ON b.auteur_id = a.id ";

    /**
     * Zoek leningen op basis van filters
     *
     * @param gebruikerId id van de gebruiker
     * @param query zoekterm (titel of auteur)
     * @param genre genre filter
     * @param start vanaf datum
     * @param eind tot datum
     * @return lijst van leningen die voldoen aan de filters
     */
    public List<Lening> searchLeningen(int gebruikerId, String query, String genre, LocalDate start, LocalDate eind) {

        // Bouw SQL statement op basis van filters
        StringBuilder sql = new StringBuilder(JOIN_QUERY);
        sql.append(" WHERE l.gebruiker_id = ?");

        List<Object> params = new ArrayList<>();
        params.add(gebruikerId);

        // Zoekterm filter (titel of auteur)
        if (query != null && !query.isEmpty()) {
            sql.append(" AND (b.titel LIKE ? OR a.naam LIKE ?)");
            String zoek = "%" + query + "%";  // % = wildcard voor LIKE
            params.add(zoek);
            params.add(zoek);
        }

        // Genre filter
        if (genre != null && !genre.equalsIgnoreCase("ALLE GENRES") && !genre.isEmpty()) {
            sql.append(" AND b.genre = ?");
            params.add(genre);
        }

        // Datum filters
        if (start != null) {
            sql.append(" AND l.uitleendatum >= ?");
            params.add(start);
        }
        if (eind != null) {
            sql.append(" AND l.uitleendatum <= ?");
            params.add(eind);
        }

        // Voer query uit en map naar Lening objecten
        return jdbcTemplate.query(sql.toString(), loanRowMapper, params.toArray());
    }

    public void saveLening(int gebruikerId, int boekId, LocalDate inleverdatum) {
        String sql = "INSERT INTO leningen (gebruiker_id, boek_id, uitleendatum, inleverdatum, is_ingeleverd) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                gebruikerId,
                boekId,
                LocalDate.now(), // Vandaag is de uitleendatum
                inleverdatum,
                false            // Boek is nog niet ingeleverd
        );
    }
}