package ru.yandex.practicum.filmorate.storage.Mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "select m.* " +
                "from mpa as m;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createMpa(rs));
    }

    @Override
    public Mpa getMpaById(int id) {
        findMpa(id);
        String sql = "select m.* " +
                " from mpa as m " +
                "where m.mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> createMpa(rs), id);
    }

    private Mpa createMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("mpa_id");
        String name = rs.getString("name");
        return Mpa.builder()
                .id(id)
                .name(name)
                .build();
    }

    private void findMpa(int mpaId) {
        String sql = "select m.* " +
                " from mpa as m " +
                "where m.mpa_id = ?";
        SqlRowSet rsMpa = jdbcTemplate.queryForRowSet(sql,
                mpaId);
        if (!rsMpa.next()) {
            throw new NotFoundException("Mpa not found");
        }
    }
}
