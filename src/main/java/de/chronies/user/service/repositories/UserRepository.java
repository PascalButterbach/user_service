package de.chronies.user.service.repositories;

import de.chronies.user.service.exceptions.ApiResponseBase;
import de.chronies.user.service.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository implements ObjectRepository<User> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<User> rowMapper = (rs, rowNum) -> User.builder()
            .user_id(rs.getInt("user_id"))
            .user_name(rs.getString("user_name"))
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .created(rs.getObject("created", LocalDateTime.class))
            .changed(rs.getObject("changed", LocalDateTime.class))
            .active(rs.getBoolean("active"))
            .build();

/*    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM [user]";
        return jdbcTemplate.query(sql, rowMapper);
    }*/


/*
    @Override
    public Optional<User> get(long id) throws DataAccessException {
        String sql = "SELECT * FROM user_service.user WHERE user_id = ?";

        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            //TODO : Logging
        }

        return Optional.ofNullable(user);
    }
*/

/*
    @Override
    public boolean update(User user, long id) {
        String sql = "UPDATE user_service.user SET user_name = ?, password = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql, user.getUser_name(), user.getPassword(), id) > 0;
    }
*/

/*
    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM user_service.user WHERE user_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
*/
    @Override
    public boolean update(User user) {
        String sql = "UPDATE user_service.user " +
                     "SET user_name = ?, " +
                     "password = ?, " +
                     "email = ?, " +
                     "changed = current_timestamp " +
                     "WHERE user_id = ?";

        boolean result;

        try {
            result = jdbcTemplate.update(sql,
                    user.getUser_name(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getUser_id()) > 0;
        } catch (DuplicateKeyException e){
            throw new ApiResponseBase("Email in use. No changes applied to your account.", HttpStatus.NOT_ACCEPTABLE);
        }
        catch (DataAccessException e) {
            throw new ApiResponseBase("Something went wrong. Contact support or try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;


    }

    @Override
    public boolean create(User user) {
        String sql = "INSERT INTO user_service.user VALUES (default,?,?,?,current_timestamp,null,true)";

        boolean result;

        try {
            result = jdbcTemplate.update(sql,
                    user.getEmail(),
                    user.getUser_name(),
                    user.getPassword()) > 0;
        } catch (DuplicateKeyException e){
            throw new ApiResponseBase("Email in use. Try registering with another email.", HttpStatus.NOT_ACCEPTABLE);
        }
        catch (DataAccessException e) {
            throw new ApiResponseBase("Something went wrong. Contact support or try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    public Optional<User> findUserByEmail(String email) throws DataAccessException {
        String sql = "SELECT * FROM user_service.user WHERE email = ?";

        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, rowMapper, email);
        } catch (Exception e) {
            // no actions required -> return empty optional
        }

        return Optional.ofNullable(user);

    }
}
