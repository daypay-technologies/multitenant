package com.daypaytechnologies.core.service;

import java.util.List;

import com.daypaytechnologies.security.service.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class PaginationHelper<E> {

    public Page<E> fetchPage(final JdbcTemplate jt, final String sqlCountRows, final String sqlFetchRows, final Object args[],
                             final RowMapper<E> rowMapper) {

        final List<E> items = jt.query(sqlFetchRows, args, rowMapper);

        // determine how many rows are available
        @SuppressWarnings("deprecation")
        final int totalFilteredRecords = jt.queryForInt(sqlCountRows);

        return new Page<>(items, totalFilteredRecords);
    }

    public Page<Long> fetchPage(JdbcTemplate jdbcTemplate, String sql, String sqlCountRows, Class<Long> type) {
        final List<Long> items = jdbcTemplate.queryForList(sql, type);

        // determine how many rows are available
        @SuppressWarnings("deprecation")
        final int totalFilteredRecords = jdbcTemplate.queryForInt(sqlCountRows);

        return new Page<>(items, totalFilteredRecords);
    }
}