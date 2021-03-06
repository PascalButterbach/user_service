package de.chronies.user.service.repository;

import de.chronies.user.service.exceptions.ApiException;
import de.chronies.user.service.model.User;
import de.chronies.user.service.rowmapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository implements ObjectRepository<User> {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper rowMapper;

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
            throw new ApiException("Email in use. No changes applied to your account.", HttpStatus.NOT_ACCEPTABLE);
        }
        catch (DataAccessException e) {
            throw new ApiException("Something went wrong. Contact support or try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
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
            throw new ApiException("Email in use. Try registering with a different email.", HttpStatus.NOT_ACCEPTABLE);
        }
        catch (DataAccessException e) {
            throw new ApiException("Something went wrong. Contact support or try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @Override
    public Optional<User> get(int id) {
        String sql = "SELECT * FROM user_service.user WHERE user_id = ?";

        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            // no actions required -> return empty optional
        }

        return Optional.ofNullable(user);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM user_service.user WHERE user_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
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
