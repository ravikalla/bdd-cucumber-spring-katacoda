package in.ravikalla.onlineacc.service.UserServiceImpl;

import static in.ravikalla.onlineacc.util.AppConstants.EXTERNAL_BANK_URL;
import static in.ravikalla.onlineacc.util.AppConstants.EXTERNAL_BANK_URL_DEPOSIT;
import static in.ravikalla.onlineacc.util.AppConstants.EXTERNAL_BANK_URL_WITHDRAW;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import in.ravikalla.onlineacc.dao.PrimaryAccountDao;
import in.ravikalla.onlineacc.dao.SavingsAccountDao;
import in.ravikalla.onlineacc.domain.PrimaryAccount;
import in.ravikalla.onlineacc.domain.PrimaryTransaction;
import in.ravikalla.onlineacc.domain.SavingsAccount;
import in.ravikalla.onlineacc.domain.SavingsTransaction;
import in.ravikalla.onlineacc.domain.User;
import in.ravikalla.onlineacc.service.AccountService;
import in.ravikalla.onlineacc.service.TransactionService;
import in.ravikalla.onlineacc.service.UserService;
@Service
public class AccountServiceImpl implements AccountService {
	private static final Logger L = LogManager.getLogger(AccountServiceImpl.class);

	private static int nextAccountNumber = 11223145;

    @Autowired
    private PrimaryAccountDao primaryAccountDao;

    @Autowired
    private SavingsAccountDao savingsAccountDao;

    @Autowired
    private UserService userService;
    
    @Autowired
    private TransactionService transactionService;

    public PrimaryAccount createPrimaryAccount() {
    		int intAccNum = 0;
    		boolean blnAccountExists = true;
    		List<PrimaryAccount> lstFindByAccountNumber;
    		while (blnAccountExists) {
    			intAccNum = accountGen();
        		lstFindByAccountNumber = primaryAccountDao.findByAccountNumber(intAccNum);
    			if (CollectionUtils.isEmpty(lstFindByAccountNumber))
    				blnAccountExists = false;
    		}

        PrimaryAccount primaryAccount = new PrimaryAccount();
        primaryAccount.setAccountBalance(new BigDecimal("0.0"));
        primaryAccount.setAccountNumber(intAccNum);

        primaryAccountDao.save(primaryAccount);

        L.debug("63 : Primary account number = {}", primaryAccount.getAccountNumber());
        lstFindByAccountNumber = primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
        if (!CollectionUtils.isEmpty(lstFindByAccountNumber))
        		return lstFindByAccountNumber.get(0);
        else
        		return null;
    }

    public SavingsAccount createSavingsAccount() {
		int intAccNum = 0;
		boolean blnAccountExists = true;
		List<SavingsAccount> lstFindByAccountNumber;
		while (blnAccountExists) {
			intAccNum = accountGen();
    			lstFindByAccountNumber = savingsAccountDao.findByAccountNumber(intAccNum);
			if (CollectionUtils.isEmpty(lstFindByAccountNumber))
				blnAccountExists = false;
		}

        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setAccountBalance(new BigDecimal("0.0"));
        savingsAccount.setAccountNumber(intAccNum);

        savingsAccountDao.save(savingsAccount);

        L.debug("88 : Savings account number = {}", savingsAccount.getAccountNumber());
        lstFindByAccountNumber = savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
        if (!CollectionUtils.isEmpty(lstFindByAccountNumber))
        		return lstFindByAccountNumber.get(0);
        else
        		return null;
    }
    
    public void deposit(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);

            callBofaDeposit();
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);

            callBofaDeposit();
        }
    }

    public void withdraw(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
            callBofaWithdraw();
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
            callBofaWithdraw();
        }
    }

    private int accountGen() {
        return ++nextAccountNumber;
    }

	private void callBofaDeposit() {
		try {
			RestTemplate rest = new RestTemplate();
			String quote = rest.getForObject(EXTERNAL_BANK_URL + EXTERNAL_BANK_URL_DEPOSIT, String.class);
			L.info("bofa online deposit response: " + quote);
		} catch (RestClientException e) {
			L.error("Rest call to Bofa-online deposit service failed.");
		}
	}

	private void callBofaWithdraw() {
		try {
			RestTemplate rest = new RestTemplate();
			String str = rest.getForObject(EXTERNAL_BANK_URL + EXTERNAL_BANK_URL_WITHDRAW, String.class);
			L.info("Bofa online response: " + str);
		} catch (RestClientException e) {
			L.error("Rest call to Bofa-online withdraw service failed.");
		}
	}
}
