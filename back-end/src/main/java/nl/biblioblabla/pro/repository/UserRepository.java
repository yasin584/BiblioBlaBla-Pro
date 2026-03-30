package nl.biblioblabla.pro.repository;

import nl.biblioblabla.pro.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@repository
public class UserRepository {

    @Autowired
    private jdbcTemplate jdbcTemplate;
    
}
