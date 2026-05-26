package bank.infrastructure.out.adapter;

import bank.application.ports.CheckingAccountRepository;
import bank.domain.CheckingAccount;
import bank.infrastructure.out.mapper.RowMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckingAccountRepositoryDb implements CheckingAccountRepository {

    private final Connection connection;
    private final RowMapper<CheckingAccount> rowMapper;

    public CheckingAccountRepositoryDb(Connection connection, RowMapper<CheckingAccount> rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public void save(CheckingAccount account) {
        String sql = "INSERT INTO accounts (account_number, client_id, balance, date_opened, " +
                "account_state, account_type, overdraft_percentage, overdraft_limit) " +
                "VALUES (?, ?, ?, ?, ?, 'CHECKING', ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, account.getAccountNumber());
            ps.setInt(2, account.getClientId());
            ps.setBigDecimal(3, account.getBalance());
            ps.setDate(4, Date.valueOf(account.getDateOpened()));
            ps.setString(5, account.getAccountState().name());
            ps.setDouble(6, account.getOverdraftPercentage());
            ps.setBigDecimal(7, account.getOverdraftLimit());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la cuenta corriente", e);
        }
    }

    @Override
    public CheckingAccount findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ? AND account_type = 'CHECKING'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la cuenta: " + accountNumber, e);
        }
        return null; // O puedes lanzar una Exception si prefieres
    }

    @Override
    public List<CheckingAccount> findAll() {
        List<CheckingAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE account_type = 'CHECKING'";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                accounts.add(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las cuentas", e);
        }
        return accounts;
    }

    @Override
    public void update(CheckingAccount account) {
        String sql = "UPDATE accounts SET balance = ?, overdraft_percentage = ?, overdraft_limit = ?, account_state = ? " +
                "WHERE account_number = ? AND account_type = 'CHECKING'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, account.getBalance());
            ps.setDouble(2, account.getOverdraftPercentage());
            ps.setBigDecimal(3, account.getOverdraftLimit());
            ps.setString(4, account.getAccountState().name());
            ps.setString(5, account.getAccountNumber());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la cuenta", e);
        }
    }

    @Override
    public void delete(String accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ? AND account_type = 'CHECKING'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la cuenta", e);
        }
    }
}