package in.ravikalla.onlineacc.service;

import java.security.Principal;
import java.util.List;

import in.ravikalla.onlineacc.domain.PrimaryAccount;
import in.ravikalla.onlineacc.domain.PrimaryTransaction;
import in.ravikalla.onlineacc.domain.Recipient;
import in.ravikalla.onlineacc.domain.SavingsAccount;
import in.ravikalla.onlineacc.domain.SavingsTransaction;

public interface TransactionService {
    
	List<PrimaryTransaction> findPrimaryTransactionList(String username);

    List<SavingsTransaction> findSavingsTransactionList(String username);

    void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);

    void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);
    
    void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
    
    void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);
    
    void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception;
    
    List<Recipient> findRecipientList(Principal principal);

    Recipient saveRecipient(Recipient recipient);

    Recipient findRecipientByName(String recipientName);

    void deleteRecipientByName(String recipientName);
    
    void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount);
}
