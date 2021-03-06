package de.chronies.user.service.repository;

import de.chronies.user.service.exceptions.ApiException;
import de.chronies.user.service.model.RefreshToken;
import de.chronies.user.service.rowmapper.TokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenRepository implements ObjectRepository<RefreshToken> {

    private final JdbcTemplate jdbcTemplate;
    private final TokenMapper rowMapper;

    public List<RefreshToken> findTokenByUserId(int userId) {
        String sql = "SELECT * FROM user_service.refresh_token WHERE user_id=?";

        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    public List<RefreshToken> findActiveTokenByUserId(int userId) {
        String sql = "SELECT * FROM user_service.refresh_token WHERE user_id=? AND revoked=false";

        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    public Optional<RefreshToken> findRefreshTokenByRefreshToken(String token) {
        String sql = "SELECT * FROM user_service.refresh_token WHERE token= ?";

        RefreshToken refreshToken = null;
        try {
            refreshToken = jdbcTemplate.queryForObject(sql, rowMapper, token);
        } catch (Exception e) {
            // no actions required -> return empty optional
        }

        return Optional.ofNullable(refreshToken);
    }

    @Override
    public boolean update(RefreshToken refreshToken) {
        String sql = "UPDATE user_service.refresh_token SET " +
                "revoked = ? " +
                "WHERE token = ? ";

        var result = jdbcTemplate.update(sql, refreshToken.isRevoked(), refreshToken.getToken());

        return result > 0;
    }

    @Override
    public boolean create(RefreshToken refreshToken) {
        String sql = "INSERT INTO user_service.refresh_token VALUES (default,?,?,?,?,false)";

        boolean result;

        try {
            result = jdbcTemplate.update(sql,
                    refreshToken.getUser_id(),
                    refreshToken.getToken(),
                    refreshToken.getCreated(),
                    refreshToken.getExpired()) > 0;
        } catch (DataAccessException e) {
            throw new ApiException("Something went wrong. Contact support or try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @Override
    public Optional<RefreshToken> get(int id) {
        String sql = "SELECT * FROM user_service.refresh_token WHERE token_id = ?";

        RefreshToken refreshToken = null;
        try {
            refreshToken = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            // no actions required -> return empty optional
        }

        return Optional.ofNullable(refreshToken);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM user_service.refresh_token WHERE token_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}
