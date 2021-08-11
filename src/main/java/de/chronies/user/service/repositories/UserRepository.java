package de.chronies.user.service.repositories;

import de.chronies.user.service.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository implements ObjectRepository<User> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<User> rowMapper = (rs, rowNum) -> User.builder()
            .user_id(rs.getLong("user_id"))
            .user_name(rs.getString("user_name"))
            .password(rs.getString("password"))
            .build();

/*    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM [user]";
        return jdbcTemplate.query(sql, rowMapper);
    }*/

    @Override
    public boolean create(User user) {
        String sql = "INSERT INTO [user] (user_name, password ) VALUES (?,?)";
        return jdbcTemplate.update(sql, user.getUser_name(), user.getPassword()) > 0;
    }

    @Override
    public Optional<User> get(long id) throws DataAccessException {
        String sql = "SELECT * FROM [user] WHERE user_id = ?";

        User user = null;
        user = jdbcTemplate.queryForObject(sql, rowMapper, id);

        return Optional.ofNullable(user);
    }

    @Override
    public boolean update(User user, long id) {
        String sql = "UPDATE [user] SET user_name = ?, password = ? WHERE user_id = ?";
        return  jdbcTemplate.update(sql,user.getUser_name(),user.getPassword(),id) > 0;
    }

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM [user] WHERE user_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public Optional<User> findByUserName(String userName) throws DataAccessException {
        String sql = "SELECT * FROM [user] WHERE user_name = ?";

        User user = null;
        user = jdbcTemplate.queryForObject(sql, rowMapper, userName);

        return Optional.ofNullable(user);
    }
}
