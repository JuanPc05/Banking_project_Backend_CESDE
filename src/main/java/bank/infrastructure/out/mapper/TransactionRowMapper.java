package bank.infrastructure.out.mapper;

import bank.domain.Transaction;
import bank.domain.enums.TransactionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();

        transaction.setId(rs.getInt("id"));
        transaction.setAccountNumber(rs.getString("account_number"));

        Timestamp sqlTimestamp = rs.getTimestamp("transaction_date");
        if (sqlTimestamp != null) {
            transaction.setTimestamp(sqlTimestamp.toLocalDateTime());
        }

        String typeStr = rs.getString("transaction_type");
        if (typeStr != null) {
            transaction.setTransactionType(TransactionType.valueOf(typeStr.toUpperCase()));
        }

        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setBalanceAfterTransaction(rs.getBigDecimal("balance_after_transaction"));
        transaction.setDescription(rs.getString("description"));

        return transaction;
    }
}