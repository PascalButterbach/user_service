package de.chronies.user.service.repositories;

import de.chronies.user.service.exceptions.ApiResponseBase;
import de.chronies.user.service.models.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TokenRepository implements ObjectRepository<RefreshToken> {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<RefreshToken> rowMapper = (rs, rowNum) -> RefreshToken.builder()
            .token_id(rs.getInt("token_id"))
            .user_id(rs.getInt("user_id"))
            .token(rs.getString("token"))
            .created(rs.getObject("created", Date.class))
            .expired(rs.getObject("expired", Date.class))
            .revoked(rs.getBoolean("revoked"))
            .build();

    public List<RefreshToken> getActiveTokenByUserId(int userId) {
        String sql = "SELECT * FROM user_service.refresh_token WHERE user_id=" + userId + " AND revoked=false";

        return jdbcTemplate.query(sql, rowMapper);
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
            throw new ApiResponseBase("Something went wrong. Contact support or try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }


}