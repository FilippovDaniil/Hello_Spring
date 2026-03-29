package Start.controller.dto;

import Start.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AccountResponseDTO {

    private Long accountId;

    private String name;

    private String email;

    private Integer bill;

    public AccountResponseDTO(Account account){
        this.accountId = account.getId();
        this.name = account.getName();
        this.email = account.getEmail();
        this.bill = account.getBill();
    }


}
