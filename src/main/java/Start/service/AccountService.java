package Start.service;

import Start.entity.Account;
import Start.exception.AccountNotFoundException;
import Start.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Long createAccount(String name, String email, Integer bill){
        Account account = new Account(name, email, bill);
        return accountRepository.save(account).getId();
    }

    public Account getAccountById(Long id){
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
    }

    public List<Account> getAll(){
        return accountRepository.findAll();
    }

    public Account delete(Long id){
        Account accountById = getAccountById(id);
        accountRepository.deleteById(id);
        return accountById;
    }

}
