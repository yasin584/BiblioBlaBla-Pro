package nl.biblioblabla.pro.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BeoordelingRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean voegBeoordelingToe(int leningId, int gebruikerId, int rating) {
        // We updaten de bestaande lening.
        // De WHERE check garandeert dat alleen de eigenaar van de lening dit kan doen.
        String sql = "UPDATE leningen SET beoordeling = ? WHERE id = ? AND gebruiker_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, rating, leningId, gebruikerId);

        return rowsAffected > 0;
    }
}