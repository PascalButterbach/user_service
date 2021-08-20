package de.chronies.user.service.rowmapper;

import de.chronies.user.service.models.RefreshToken;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Component
public class TokenMapper implements RowMapper<RefreshToken> {

    @Override
    public RefreshToken mapRow(ResultSet resultSet, int i) throws SQLException {
        return RefreshToken.builder()
                .token_id(resultSet.getInt("token_id"))
                .user_id(resultSet.getInt("user_id"))
                .token(resultSet.getString("token"))
                .created(resultSet.getObject("created", Date.class))
                .expired(resultSet.getObject("expired", Date.class))
                .revoked(resultSet.getBoolean("revoked"))
                .build();
    }

}
