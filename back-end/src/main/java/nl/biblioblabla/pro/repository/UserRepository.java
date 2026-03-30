package nl.biblioblabla.pro.repository;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    
    // Lombok zorgt automatisch voor de constructor en dependency injection
    private final JdbcTemplate jdbcTemplate;

    // Deze RowMapper vertaalt een rij uit de 'gebruikers' tabel naar jouw User object
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id")); 
        user.setNaam(rs.getString("naam")); 
        user.setEmail(rs.getString("email"));
        user.setWachtwoordHash(rs.getString("wachtwoord_hash")); 
        return user;
    };

    // Zoeken op e-mail voor het inloggen
    public User findByEmail(String email) {
        String sql = "SELECT * FROM gebruikers WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null; // Geeft null terug als het e-mailadres niet bestaat
        }
    }

    // Zoeken op ID (handig voor het ophalen van gebruikersprofielen)
    public User findById(long id) {
        String sql = "SELECT * FROM gebruikers WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null; // Geeft null terug als de ID niet bestaat
        }
    }
}