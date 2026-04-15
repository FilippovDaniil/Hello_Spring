package Start.controller;

import Start.entity.Account;
import Start.exception.AccountNotFoundException;
import Start.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    // ─── GET /hello ───────────────────────────────────────────────────────────

    @Test
    void hello_returns200WithHelloSpringMessage() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Spring !"));
    }

    // ─── POST /accounts ───────────────────────────────────────────────────────

    @Test
    void createAccount_returns200WithGeneratedId() throws Exception {
        when(accountService.createAccount("Alice", "alice@mail.com", 1000)).thenReturn(1L);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alice","email":"alice@mail.com","bill":1000}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(accountService).createAccount("Alice", "alice@mail.com", 1000);
    }

    // ─── GET /accounts/{id} ───────────────────────────────────────────────────

    @Test
    void getAccount_returns200WithAccountResponseDTO_whenAccountExists() throws Exception {
        Account account = new Account("Bob", "bob@mail.com", 500);
        account.setId(2L);
        when(accountService.getAccountById(2L)).thenReturn(account);

        mockMvc.perform(get("/accounts/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(2))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@mail.com"))
                .andExpect(jsonPath("$.bill").value(500));
    }

    @Test
    void getAccount_throwsAccountNotFoundException_whenAccountNotFound() {
        when(accountService.getAccountById(99L))
                .thenThrow(new AccountNotFoundException("Account not found with id: 99"));

        // Без глобального @ExceptionHandler Spring MVC пробрасывает исключение наружу
        assertThatThrownBy(() -> mockMvc.perform(get("/accounts/99")))
                .hasRootCauseInstanceOf(AccountNotFoundException.class)
                .hasRootCauseMessage("Account not found with id: 99");
    }

    // ─── GET /accounts ────────────────────────────────────────────────────────

    @Test
    void getAll_returns200WithListOfAccountResponseDTOs() throws Exception {
        Account a1 = new Account("Alice", "alice@mail.com", 100);
        a1.setId(1L);
        Account a2 = new Account("Bob", "bob@mail.com", 200);
        a2.setId(2L);
        when(accountService.getAll()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].accountId").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].bill").value(100))
                .andExpect(jsonPath("$[1].accountId").value(2))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].bill").value(200));
    }

    @Test
    void getAll_returns200WithEmptyArray_whenNoAccountsExist() throws Exception {
        when(accountService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // ─── DELETE /accounts/{id} ────────────────────────────────────────────────

    @Test
    void delete_returns200WithDeletedAccountResponseDTO_whenAccountExists() throws Exception {
        Account account = new Account("Charlie", "charlie@mail.com", 300);
        account.setId(3L);
        when(accountService.delete(3L)).thenReturn(account);

        mockMvc.perform(delete("/accounts/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(3))
                .andExpect(jsonPath("$.name").value("Charlie"))
                .andExpect(jsonPath("$.email").value("charlie@mail.com"))
                .andExpect(jsonPath("$.bill").value(300));
    }

    @Test
    void delete_throwsAccountNotFoundException_whenAccountNotFound() {
        when(accountService.delete(99L))
                .thenThrow(new AccountNotFoundException("Account not found with id: 99"));

        // Без глобального @ExceptionHandler Spring MVC пробрасывает исключение наружу
        assertThatThrownBy(() -> mockMvc.perform(delete("/accounts/99")))
                .hasRootCauseInstanceOf(AccountNotFoundException.class)
                .hasRootCauseMessage("Account not found with id: 99");
    }
}
