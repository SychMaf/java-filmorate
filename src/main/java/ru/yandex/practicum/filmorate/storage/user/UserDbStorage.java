package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@Qualifier("UserDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        String sql = "select u.*, " +
                "from clients as u ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createUser(rs));
    }

    @Override
    public User create(User user) {
        String sql = "insert into clients (email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        findUser(user.getId());
        String sql = "update clients " +
                "set email = ?, login = ?, name = ?, birthday = ? " +
                "where user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return findById(user.getId());
    }

    @Override
    public User findById(Integer userId) {
        findUser(userId);
        String sql = "select u.*, " +
                "from clients as u " +
                "where u.user_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> createUser(rs), userId);
    }

    @Override
    public boolean addFriend(int firstUserId, int secondUserId) {
        findUser(firstUserId);
        findUser(secondUserId);
        String sql = "merge into friendship key(first_user_id, second_user_id) " +
                "values(?, ?)";
        return jdbcTemplate.update(sql, firstUserId, secondUserId) > 0;
    }

    @Override
    public boolean deleteFriend(int firstUserId, int secondUserId) {
        String sql = "delete from friendship " +
                "where first_user_id = ? and second_user_id = ?";
        return jdbcTemplate.update(sql, firstUserId, secondUserId) > 0;
    }

    @Override
    public List<Integer> getFriendList(int userId) {
        String sql = "select second_user_id from friendship as f " +
                "where first_user_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }


    private User createUser(ResultSet rs) throws SQLException {
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        int id = rs.getInt("user_id");
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }

    private void findUser(int userId) {
        String sql = "select user_id from clients as c " +
                "where user_id = ?";
        SqlRowSet rsUser = jdbcTemplate.queryForRowSet(sql,
                userId);
        if (!rsUser.next()) {
            throw new NotFoundException("User not found");
        }
    }
}