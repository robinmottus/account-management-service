package accountManagement.service;

import accountManagement.domain.AccountEntity;
import accountManagement.dto.AccountCreateRequest;
import accountManagement.dto.AccountResponse;
import accountManagement.dto.AccountUpdateRequest;
import accountManagement.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountResponse saveAccount(final AccountCreateRequest account) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName(account.getName());
        accountEntity.setPhoneNr(account.getPhoneNr());
        accountEntity = accountRepository.save(accountEntity);

        return mapResponse(accountEntity);
    }

    public AccountResponse getAccount(final Long id) {
       return accountRepository.findById(id)
               .map(this::mapResponse)
               .orElseThrow(() -> new EntityNotFoundException("Account not found!"));
    }

    @Transactional
    public AccountResponse updateAccount(final Long id, final AccountUpdateRequest account) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found!"));
        accountEntity.setName(account.getName());
        accountEntity.setPhoneNr(account.getPhoneNr());
        accountEntity = accountRepository.save(accountEntity);

        return mapResponse(accountEntity);
    }

    public void deleteAccount(final Long id) {
        if (!accountRepository.existsById(id)) {
            throw new EntityNotFoundException("Account not found!");
        }
        accountRepository.deleteById(id);
    }

    private AccountResponse mapResponse(final AccountEntity accountEntity) {
        return AccountResponse.builder()
                .id(accountEntity.getId())
                .name(accountEntity.getName())
                .phoneNr(accountEntity.getPhoneNr())
                .createdDtime(accountEntity.getCreatedDtime())
                .modifiedDtime(accountEntity.getModifiedDtime())
                .build();
    }
}
