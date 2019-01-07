package in.ravikalla.onlineacc.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import in.ravikalla.onlineacc.domain.SavingsTransaction;

public interface SavingsTransactionDao extends CrudRepository<SavingsTransaction, Long> {

    List<SavingsTransaction> findAll();
}

