package de.chronies.user.service.rowmapper;

import de.chronies.user.service.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return User.builder()
                .user_id(resultSet.getInt("user_id"))
                .user_name(resultSet.getString("user_name"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .created(resultSet.getObject("created", LocalDateTime.class))
                .changed(resultSet.getObject("changed", LocalDateTime.class))
                .active(resultSet.getBoolean("active"))
                .build();
    }

}
