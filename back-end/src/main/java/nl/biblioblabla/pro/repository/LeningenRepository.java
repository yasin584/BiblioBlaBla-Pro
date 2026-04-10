package nl.biblioblabla.pro.repository;

import nl.biblioblabla.pro.model.Lening;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LeningenRepository {

    private final JdbcTemplate jdbcTemplate;

    public LeningenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper voor Lening
    private final RowMapper<Lening> loanRowMapper = (rs, rowNum) -> {
        Lening loan = new Lening();
        loan.setId(rs.getInt("id"));
        loan.setGebruikerId(rs.getInt("gebruiker_id"));
        loan.setBoekId(rs.getInt("boek_id"));

        if (rs.getDate("uitleendatum") != null) {
            loan.setUitleendatum(rs.getDate("uitleendatum").toLocalDate());
        }
        if (rs.getDate("inleverdatum") != null) {
            loan.setInleverdatum(rs.getDate("inleverdatum").toLocalDate());
        }

        int beoordeling = rs.getInt("beoordeling");
        loan.setBeoordeling(rs.wasNull() ? null : beoordeling);

        loan.setIngeleverd(rs.getBoolean("is_ingeleverd"));

        loan.setTitel(rs.getString("titel"));
        loan.setGenre(rs.getString("genre"));
        loan.setAvgRating(rs.getDouble("gemiddelde_beoordeling"));
        loan.setAuteurNaam(rs.getString("auteur_naam"));

        return loan;
    };

    private final String JOIN_QUERY =
            "SELECT l.*, b.titel, b.genre, b.gemiddelde_beoordeling, a.naam AS auteur_naam " +
                    "FROM leningen l " +
                    "LEFT JOIN boeken b ON l.boek_id = b.id " +
                    "LEFT JOIN auteurs a ON b.auteur_id = a.id ";

    // Zoeken van leningen met filters
    public List<Lening> searchLeningen(int gebruikerId, String query, String genre, LocalDate start, LocalDate eind) {
        try {
            StringBuilder sql = new StringBuilder(JOIN_QUERY);
            List<Object> params = new ArrayList<>();
            sql.append(" WHERE l.gebruiker_id = ?");
            params.add(gebruikerId);

            if (query != null && !query.isEmpty()) {
                sql.append(" AND (b.titel LIKE ? OR a.naam LIKE ?)");
                String zoek = "%" + query + "%";
                params.add(zoek);
                params.add(zoek);
            }

            if (genre != null && !genre.equalsIgnoreCase("ALLE GENRES") && !genre.isEmpty()) {
                sql.append(" AND b.genre = ?");
                params.add(genre);
            }

            if (start != null) {
                sql.append(" AND l.uitleendatum >= ?");
                params.add(java.sql.Date.valueOf(start));
            }
            if (eind != null) {
                sql.append(" AND l.uitleendatum <= ?");
                params.add(java.sql.Date.valueOf(eind));
            }

            List<Lening> leningen = jdbcTemplate.query(sql.toString(), loanRowMapper, params.toArray());

            if (leningen.isEmpty()) {
                if (genre != null && !genre.equalsIgnoreCase("ALLE GENRES")) {
                    throw new GenreNietGevondenException(genre);
                }
                if (start != null || eind != null) {
                    throw new GeenLeningenVoorDatumException(start, eind);
                }
                throw new GeenLeningenVoorGebruikerException(gebruikerId);
            }

            return leningen;

        } catch (DataAccessException ex) {
            throw new LeningenRepositoryException("Fout bij ophalen van leningen: " + ex.getMessage());
        }
    }

    public void inleverenLening(int leningId, int gebruikerId) {
        String sql = "UPDATE leningen SET is_ingeleverd = true, inleverdatum = ? " +
                "WHERE id = ? AND gebruiker_id = ?";

        int rows = jdbcTemplate.update(sql, LocalDate.now(), leningId, gebruikerId);

        if (rows == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lening niet gevonden of niet van jou.");
        }
    }

    public void saveLening(int gebruikerId, int boekId, LocalDate inleverdatum) {
        String sql = "INSERT INTO leningen (gebruiker_id, boek_id, uitleendatum, inleverdatum, is_ingeleverd) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                gebruikerId,
                boekId,
                java.sql.Date.valueOf(LocalDate.now()),
                java.sql.Date.valueOf(inleverdatum),
                false
        );
    }
}