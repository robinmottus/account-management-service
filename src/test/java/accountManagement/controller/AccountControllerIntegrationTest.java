package accountManagement.controller;

import accountManagement.domain.AccountEntity;
import accountManagement.dto.AccountCreateRequest;
import accountManagement.dto.AccountResponse;
import accountManagement.dto.AccountUpdateRequest;
import accountManagement.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void createAccountSavesToDbWithValidInput() throws Exception {
        //given
        AccountCreateRequest request = new AccountCreateRequest("Test name", "+37255333058");

        //when
        MvcResult result = mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test name"))
                .andExpect(jsonPath("$.phoneNr").value("+37255333058"))
                .andReturn();

        //then
        AccountResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), AccountResponse.class);

        assertTrue(accountRepository.existsById(response.getId()));
        AccountEntity savedAccount = accountRepository.findById(response.getId()).orElseThrow();
        assertEquals("Test name", savedAccount.getName());
        assertEquals("+37255333058", savedAccount.getPhoneNr());
    }

    @Test
    void createAccountThrowsExceptionWithInvalidName() throws Exception {
        //given
        AccountCreateRequest request = new AccountCreateRequest("", "+37255333058");

        //when
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required!"));

        //then
        assertEquals(0, accountRepository.count());
    }

    @Test
    void createAccountThrowsExceptionWithPhoneNr() throws Exception {
        //given
        AccountCreateRequest request = new AccountCreateRequest("Test name", "333058");

        //when
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.phoneNr").value("Invalid phone number!"));

        //then
        assertEquals(0, accountRepository.count());
    }

    @Test
    void getAccountWorksWithExistingAccount() throws Exception {
        //given
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName("Test name");
        accountEntity.setPhoneNr("+37255333058");
        AccountEntity savedAccount = accountRepository.save(accountEntity);

        //when
        mockMvc.perform(get("/account/{id}", savedAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedAccount.getId()))
                .andExpect(jsonPath("$.name").value("Test name"))
                .andExpect(jsonPath("$.phoneNr").value("+37255333058"));
    }

    @Test
    void getAccountThrowsExceptionWithNonExistentAccount() throws Exception {
        //when
        mockMvc.perform(get("/account/{id}", 1234L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Account not found!"));
    }

    @Test
    void updateAccountWorksWithExistingAccount() throws Exception {
        //given
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName("Test name");
        accountEntity.setPhoneNr("+37255333058");
        AccountEntity savedAccount = accountRepository.save(accountEntity);

        AccountUpdateRequest request = new AccountUpdateRequest(null, "Another name", "+37255444056");

        //when
        mockMvc.perform(put("/accounts/{id}", savedAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        //then
        AccountEntity updatedAccount = accountRepository.findById(savedAccount.getId()).orElseThrow();
        assertEquals("Another name", updatedAccount.getName());
        assertEquals("+37255444056", updatedAccount.getPhoneNr());
    }

    @Test
    void updateAccountThrowsExceptionWithNonExistentAccount() throws Exception {
        //given
        AccountUpdateRequest request = new AccountUpdateRequest(null, "Another name", "+37255444056");

        //when
        mockMvc.perform(put("/accounts/{id}", 1235L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccountWorksWithExistingAccount() throws Exception {
        //given
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName("Test name");
        accountEntity.setPhoneNr("+37255444056");
        AccountEntity savedAccount = accountRepository.save(accountEntity);

        //when
        mockMvc.perform(delete("/accounts/{id}", savedAccount.getId()))
                .andExpect(status().isNoContent());

        //then
        assertFalse(accountRepository.existsById(savedAccount.getId()));
    }

    @Test
    void deleteAccountThrowsExceptionWithNonExistentAccount() throws Exception {
        //when
        mockMvc.perform(delete("/accounts/{id}", 1236L))
                .andExpect(status().isNotFound());
    }
}
