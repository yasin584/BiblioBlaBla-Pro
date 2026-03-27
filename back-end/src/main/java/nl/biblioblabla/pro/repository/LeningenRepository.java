package nl.biblioblabla.pro.repository;

import nl.biblioblabla.pro.model.Leningen;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LeningenRepository {

    private final JdbcTemplate jdbcTemplate;

    public LeningenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // De RowMapper: De vertaler van Database-rij naar Java-Object
    private final RowMapper<Leningen> loanRowMapper = (rs, rowNum) -> {
        Leningen loan = new Leningen();
        loan.setId(rs.getInt("id"));
        loan.setGebruikerId(rs.getInt("gebruiker_id"));
        loan.setBoekId(rs.getInt("boek_id"));
        loan.setUitleendatum(rs.getDate("uitleendatum").toLocalDate());

        // De veilige check voor de datum die leeg kan zijn
        if (rs.getDate("inleverdatum") != null) {
            loan.setInleverdatum(rs.getDate("inleverdatum").toLocalDate());
        }

        loan.setBeoordeling(rs.getInt("beoordeling"));
        loan.setStatus(rs.getString("status"));
        return loan;
    };

    // METHODE 1: Alles ophalen (SELECT *)
    public List<Leningen> findAll() {
        String sql = "SELECT * FROM leningen";
        // .query() gebruikt de RowMapper om van elke rij een Leningen-object te maken
        return jdbcTemplate.query(sql, loanRowMapper);
    }

    // METHODE 2: Een nieuwe lening toevoegen (INSERT)
    public int save(Leningen loan) {
        String sql = "INSERT INTO leningen (gebruiker_id, boek_id, uitleendatum, status) VALUES (?, ?, ?, ?)";
        // .update() voert de SQL uit en vult de vraagtekens in
        return jdbcTemplate.update(sql,
                loan.getGebruikerId(),
                loan.getBoekId(),
                loan.getUitleendatum(),
                loan.getStatus());
    }

    // 3. VERWIJDEREN
    public int deleteById(int id) {
        String sql = "DELETE FROM leningen WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
