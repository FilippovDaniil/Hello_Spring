package Start.repository;

import Start.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    // ─── save / findById ─────────────────────────────────────────────────────

    @Test
    void save_persistsAccountAndGeneratesId() {
        Account account = new Account("Alice", "alice@mail.com", 1000);

        Account saved = accountRepository.save(account);

        assertThat(saved.getId()).isNotNull().isPositive();
        assertThat(saved.getName()).isEqualTo("Alice");
        assertThat(saved.getEmail()).isEqualTo("alice@mail.com");
        assertThat(saved.getBill()).isEqualTo(1000);
    }

    @Test
    void findById_returnsAccount_whenAccountExists() {
        Account saved = accountRepository.save(new Account("Bob", "bob@mail.com", 500));

        Optional<Account> found = accountRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Bob");
        assertThat(found.get().getEmail()).isEqualTo("bob@mail.com");
    }

    @Test
    void findById_returnsEmpty_whenAccountDoesNotExist() {
        Optional<Account> found = accountRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    // ─── findAll ──────────────────────────────────────────────────────────────

    @Test
    void findAll_returnsAllSavedAccounts() {
        accountRepository.save(new Account("Alice", "alice@mail.com", 100));
        accountRepository.save(new Account("Bob", "bob@mail.com", 200));

        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts).hasSize(2);
    }

    @Test
    void findAll_returnsEmptyList_whenNoAccountsSaved() {
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts).isEmpty();
    }

    // ─── deleteById ───────────────────────────────────────────────────────────

    @Test
    void deleteById_removesAccountFromDatabase() {
        Account saved = accountRepository.save(new Account("Charlie", "charlie@mail.com", 300));
        Long id = saved.getId();

        accountRepository.deleteById(id);

        assertThat(accountRepository.findById(id)).isEmpty();
    }

    @Test
    void deleteById_doesNotAffectOtherAccounts() {
        Account a1 = accountRepository.save(new Account("Alice", "alice@mail.com", 100));
        Account a2 = accountRepository.save(new Account("Bob", "bob@mail.com", 200));

        accountRepository.deleteById(a1.getId());

        assertThat(accountRepository.findById(a2.getId())).isPresent();
        assertThat(accountRepository.findAll()).hasSize(1);
    }
}
