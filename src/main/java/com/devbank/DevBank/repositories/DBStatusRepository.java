package com.devbank.DevBank.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class DBStatusRepository {

    private final JdbcTemplate jdbcTemplate;

    public DBStatusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getActiveConnections() {
        return jdbcTemplate.queryForObject(
                "SELECT count(*) FROM pg_stat_activity WHERE state = 'active'", Integer.class);
    }

    public int getTotalConnections() {
        return jdbcTemplate.queryForObject(
                "SELECT count(*) FROM pg_stat_activity", Integer.class);
    }

    public int getMaxConnections() {
        return jdbcTemplate.queryForObject(
                "SELECT setting::int FROM pg_settings WHERE name = 'max_connections'", Integer.class);
    }
}
