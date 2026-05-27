package bank.infrastructure.out.adapter;

import bank.application.ports.ISavingsAccountRepository;
import bank.domain.SavingsAccount;
import bank.infrastructure.out.mapper.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SavingsAccountRepositoryDb  implements ISavingsAccountRepository {

    private Connection connection;
    private RowMapper<SavingsAccount> rowMapper;

    public SavingsAccountRepositoryDb(Connection connection, RowMapper<SavingsAccount> rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public void save(SavingsAccount savingsAccount) {
        // Usamos 'account' (singular) y el tipo 'Cuenta de Ahorros'
        String sql = "INSERT INTO accounts (account_number, client_id, balance, date_opened, account_state, account_type, interest_rate) " +
                "VALUES (?, ?, ?, ?, ?, 'Cuenta de Ahorros', ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, savingsAccount.getAccountNumber());
            ps.setInt(2, savingsAccount.getClientId());
            ps.setBigDecimal(3, savingsAccount.getBalance());
            ps.setDate(4, Date.valueOf(savingsAccount.getDateOpened()));
            ps.setString(5, savingsAccount.getAccountState().name());
            ps.setDouble(6, savingsAccount.getInterestRate()); // Ajusta si tu variable se llama distinto

            ps.executeUpdate();
            System.out.println("💾 ¡Cuenta de Ahorros guardada en la base de datos!");
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la cuenta de ahorros", e);
        }
    } // <--- ¡ESTA ES LA LLAVE QUE TE FALTABA!

    @Override
    public Optional<SavingsAccount> findById(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ? AND account_type = 'Cuenta de Ahorros'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si encuentra datos, los envuelve en un Optional
                    return Optional.of(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la cuenta de ahorros: " + accountNumber, e);
        }
        // Si no entra al 'if', retorna un Optional vacío (evita NullPointerException)
        return Optional.empty();
    }

    @Override
    public List<SavingsAccount> findAll() {
        List<SavingsAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE account_type = 'Cuenta de Ahorros'";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                accounts.add(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las cuentas de ahorros", e);
        }
        return accounts;
    }

    @Override
    public void update(SavingsAccount savingsAccount) {
        String sql = "UPDATE accounts SET balance = ?, interest_rate = ?, account_state = ? " +
                "WHERE account_number = ? AND account_type = 'Cuenta de Ahorros'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, savingsAccount.getBalance());
            ps.setDouble(2, savingsAccount.getInterestRate());
            ps.setString(3, savingsAccount.getAccountState().name());
            ps.setString(4, savingsAccount.getAccountNumber());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la cuenta de ahorros", e);
        }
    }

    @Override
    public void delete(String accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ? AND account_type = 'Cuenta de Ahorros'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la cuenta de ahorros", e);
        }
    }
}