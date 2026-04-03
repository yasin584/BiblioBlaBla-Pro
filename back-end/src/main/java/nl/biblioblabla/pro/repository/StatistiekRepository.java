package nl.biblioblabla.pro.repository;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.exception.GenreNietBekendException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StatistiekRepository {
    private final JdbcTemplate jdbcTemplate;

    // Haalt het gemiddelde op voor een specifiek boek
    public Double berekenGemiddeldeVoorBoek(int boekId) {
        String sql = "SELECT AVG(beoordeling) FROM leningen WHERE boek_id = ? AND beoordeling IS NOT NULL";
        return jdbcTemplate.queryForObject(sql, Double.class, boekId);
    }

    // Werkt de kolom in de boeken tabel bij
    public void updateBoekGemiddelde(int boekId, Double gemiddelde) {
        String sql = "UPDATE boeken SET gemiddelde_beoordeling = ? WHERE id = ?";
        jdbcTemplate.update(sql, gemiddelde, boekId);
    }

    // Haalt de stats op voor de DTO
    public int getTotaalGeleend(int gebruikerId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM leningen WHERE gebruiker_id = ?", Integer.class, gebruikerId);
    }

    public String getPopulairsteGenre(int gebruikerId) {
        String sql = "SELECT b.genre FROM leningen l " +
                "JOIN boeken b ON l.boek_id = b.id " +
                "WHERE l.gebruiker_id = ? " +
                "GROUP BY b.genre " +
                "ORDER BY COUNT(*) DESC LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, gebruikerId);
        } catch (Exception e) {
            throw new GenreNietBekendException("Nog geen genres bekend voor gebruiker " + gebruikerId);
        }
    }

    public int getBoekIdBijLening(int leningId) {
        String sql = "SELECT boek_id FROM leningen WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, leningId);
    }
}