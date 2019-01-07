package in.ravikalla.onlineacc.dao;

import in.ravikalla.onlineacc.domain.SavingsAccount;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SavingsAccountDao extends CrudRepository<SavingsAccount, Long> {

    List<SavingsAccount> findByAccountNumber (int accountNumber);
}
