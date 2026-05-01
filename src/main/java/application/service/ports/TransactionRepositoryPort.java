package application.service.ports;

import application.domain.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryPort {
    // Solo permitimos crear y leer, garantizando la inmutabilidad del historial
    void save(Transaction transaction);
    Optional<Transaction> findById(int id);
    List<Transaction> findAll();
}