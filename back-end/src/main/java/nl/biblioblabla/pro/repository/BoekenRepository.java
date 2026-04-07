package nl.biblioblabla.pro.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class BoekenRepository {

    private final JdbcTemplate jdbcTemplate;

    public Integer getOrCreateBoek(String titel, String genre, int auteurId) {
        String selectSql = "SELECT id FROM boeken WHERE titel = ? AND auteur_id = ?";
        try {
            return jdbcTemplate.queryForObject(selectSql, Integer.class, titel, auteurId);
        } catch (EmptyResultDataAccessException e) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO boeken (titel, genre, auteur_id) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, titel);
                ps.setString(2, genre);
                ps.setInt(3, auteurId);
                return ps;
            }, keyHolder);

            return Objects.requireNonNull(keyHolder.getKey()).intValue();
        }
    }

    public List<String> searchTitels(String query) {
        String sql = "SELECT titel FROM boeken WHERE titel LIKE ? LIMIT 10";
        return jdbcTemplate.queryForList(sql, String.class, "%" + query + "%");
    }

    public String findGenreByTitel(String titel) {
        String sql = "SELECT genre FROM boeken WHERE titel = ? LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, titel);
        } catch (EmptyResultDataAccessException e) {
            // Als het boek niet bestaat, geven we null terug zodat de frontend
            // weet dat de gebruiker zelf tekst moet invoeren
            return null;
        }
    }
}