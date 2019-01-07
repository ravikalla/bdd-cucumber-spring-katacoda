package in.ravikalla.onlineacc.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import in.ravikalla.onlineacc.domain.PrimaryTransaction;

public interface PrimaryTransactionDao extends CrudRepository<PrimaryTransaction, Long> {

    List<PrimaryTransaction> findAll();
}
