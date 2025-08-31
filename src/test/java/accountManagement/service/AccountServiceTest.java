package accountManagement.service;

import accountManagement.domain.AccountEntity;
import accountManagement.dto.AccountCreateRequest;
import accountManagement.dto.AccountResponse;
import accountManagement.dto.AccountUpdateRequest;
import accountManagement.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Test
    void saveAccountValidInputWorks() {
        //given
        AccountCreateRequest request = getAccountRequest();
        AccountEntity savedAccount = getAccountEntity();

        when(accountRepository.save(any())).thenReturn(savedAccount);

        //when
        AccountResponse response = accountService.saveAccount(request);

        //then
        assertEquals("Test name", response.getName());
        assertEquals("+37255333058", response.getPhoneNr());
    }

    @Test
    void getAccountIfExists() {
        //given
        Long accountId = 1L;
        AccountEntity savedAccount = getAccountEntity();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(savedAccount));

        //when
        AccountResponse response = accountService.getAccount(accountId);

        //then
        assertNotNull(response);
        assertEquals(accountId, response.getId());
        assertEquals("Test name", response.getName());
        assertEquals("+37255333058", response.getPhoneNr());
        verify(accountRepository).findById(accountId);
    }

    @Test
    void getAccountThrowsExceptionIfAccountDoesNotExist() {
        //given
        Long id = 1234L;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> accountService.getAccount(id));
    }

    @Test
    void updateAccountWorks() {
        //given
        Long id = 1L;
        AccountUpdateRequest dto = new AccountUpdateRequest();
        dto.setName("Another name");
        dto.setPhoneNr("55444056");

        AccountEntity savedAccount = getAccountEntity();
        AccountEntity updatedAccount = new AccountEntity();
        updatedAccount.setId(id);
        updatedAccount.setName("Another name");
        updatedAccount.setPhoneNr("55444056");
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(updatedAccount);
        when(accountRepository.findById(id)).thenReturn(Optional.of(savedAccount));

        //when
        AccountResponse response = accountService.updateAccount(id, dto);

        //then
        assertEquals("Another name", response.getName());
        assertEquals("55444056", response.getPhoneNr());
    }

    @Test
    void deleteAccountWorksWithExistingId() {
        //given
        Long id = 1L;
        when(accountRepository.existsById(id)).thenReturn(true);

        //when
        accountService.deleteAccount(id);

        //then
        verify(accountRepository).existsById(id);
        verify(accountRepository).deleteById(id);
    }

    @Test
    void deleteAccountThrowsExceptionWithNonExistentId() {
        //given
        Long id = 1234L;
        when(accountRepository.existsById(id)).thenReturn(false);

        //then
        assertThrows(EntityNotFoundException.class, () -> accountService.deleteAccount(id));
    }

    private static AccountCreateRequest getAccountRequest() {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setName("Test name");
        accountCreateRequest.setPhoneNr("+37255333058");
        return accountCreateRequest;
    }

    private static AccountEntity getAccountEntity() {
        AccountEntity savedAccount = new AccountEntity();
        savedAccount.setId(1L);
        savedAccount.setName("Test name");
        savedAccount.setPhoneNr("+37255333058");
        savedAccount.setCreatedDtime(LocalDateTime.now());
        savedAccount.setModifiedDtime(LocalDateTime.now());
        return savedAccount;
    }
}
