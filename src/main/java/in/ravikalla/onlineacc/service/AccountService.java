package in.ravikalla.onlineacc.service;

import java.security.Principal;

import in.ravikalla.onlineacc.domain.PrimaryAccount;
import in.ravikalla.onlineacc.domain.SavingsAccount;

public interface AccountService {
	
    PrimaryAccount createPrimaryAccount();
    
    SavingsAccount createSavingsAccount();
    
    void deposit(String accountType, double amount, Principal principal);
    
    void withdraw(String accountType, double amount, Principal principal);
    
}
