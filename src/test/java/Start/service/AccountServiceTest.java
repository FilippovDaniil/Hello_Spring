package Start.service;

import Start.entity.Account;
import Start.exception.AccountNotFoundException;
import Start.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    // ─── createAccount ────────────────────────────────────────────────────────

    @Test
    void createAccount_savesAccountAndReturnsGeneratedId() {
        Account saved = new Account("Alice", "alice@mail.com", 1000);
        saved.setId(1L);
        when(accountRepository.save(any(Account.class))).thenReturn(saved);

        Long result = accountService.createAccount("Alice", "alice@mail.com", 1000);

        assertThat(result).isEqualTo(1L);
        verify(accountRepository).save(any(Account.class));
    }

    // ─── getAccountById ───────────────────────────────────────────────────────

    @Test
    void getAccountById_returnsAccount_whenAccountExists() {
        Account account = new Account("Bob", "bob@mail.com", 500);
        account.setId(2L);
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountById(2L);

        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Bob");
        assertThat(result.getEmail()).isEqualTo("bob@mail.com");
        assertThat(result.getBill()).isEqualTo(500);
    }

    @Test
    void getAccountById_throwsAccountNotFoundException_whenAccountDoesNotExist() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(99L))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    void getAll_returnsAllAccounts() {
        Account a1 = new Account("Alice", "alice@mail.com", 100);
        Account a2 = new Account("Bob", "bob@mail.com", 200);
        when(accountRepository.findAll()).thenReturn(List.of(a1, a2));

        List<Account> result = accountService.getAll();

        assertThat(result).hasSize(2).containsExactly(a1, a2);
    }

    @Test
    void getAll_returnsEmptyList_whenNoAccountsExist() {
        when(accountRepository.findAll()).thenReturn(List.of());

        List<Account> result = accountService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    void delete_removesAccountAndReturnsIt_whenAccountExists() {
        Account account = new Account("Charlie", "charlie@mail.com", 300);
        account.setId(3L);
        when(accountRepository.findById(3L)).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).deleteById(3L);

        Account result = accountService.delete(3L);

        assertThat(result).isEqualTo(account);
        verify(accountRepository).deleteById(3L);
    }

    @Test
    void delete_throwsAccountNotFoundException_andNeverCallsDeleteById_whenAccountDoesNotExist() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.delete(99L))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("99");

        verify(accountRepository, never()).deleteById(any());
    }
}
