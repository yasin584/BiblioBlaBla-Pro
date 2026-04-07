package nl.biblioblabla.pro.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BeoordelingRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean voegBeoordelingToe(int leningId, int gebruikerId, int rating) {
        // We updaten de beoordeling van het boek in deze specifieke lening
        String sql = "UPDATE leningen SET beoordeling = ? WHERE id = ? AND gebruiker_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, rating, leningId, gebruikerId);

        // Geeft true als de update is geslaagd
        return rowsAffected > 0;
    }
}