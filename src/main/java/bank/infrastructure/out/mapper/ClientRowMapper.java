package bank.infrastructure.out.mapper;

import bank.domain.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRowMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet rs) throws SQLException {
        Client client = new Client();

        client.setId(rs.getInt("id"));
        client.setIdentification(rs.getString("identification"));
        client.setFullName(rs.getString("fullName"));
        client.setCellPhone(rs.getString("cellPhone"));
        client.setUserName(rs.getString("userName"));
        client.setPassword(rs.getString("password"));
        client.setFailedIntents(rs.getInt("failedIntents"));
        client.setBlocked(rs.getBoolean("blocked"));

        return client;
    }
}
