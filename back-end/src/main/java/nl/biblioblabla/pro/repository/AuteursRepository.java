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
public class AuteursRepository {

    private final JdbcTemplate jdbcTemplate;

    public Integer getOrCreateAuteur(String naam) {
        String selectSql = "SELECT id FROM auteurs WHERE naam = ?";
        try {
            return jdbcTemplate.queryForObject(selectSql, Integer.class, naam);
        } catch (EmptyResultDataAccessException e) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO auteurs (naam) VALUES (?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, naam);
                return ps;
            }, keyHolder);

            return Objects.requireNonNull(keyHolder.getKey()).intValue();
        }
    }

    public List<String> searchAuteurs(String query) {
        String sql = "SELECT naam FROM auteurs WHERE naam LIKE ? LIMIT 10";
        return jdbcTemplate.queryForList(sql, String.class, "%" + query + "%");
    }
}