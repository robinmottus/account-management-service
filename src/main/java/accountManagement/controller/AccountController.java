package accountManagement.controller;

import accountManagement.dto.AccountCreateRequest;
import accountManagement.dto.AccountResponse;
import accountManagement.dto.AccountUpdateRequest;
import accountManagement.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Account management", description = "Account CRUD operations")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/accounts")
    @Operation(summary = "Create a new account")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody final AccountCreateRequest account) {
            return new ResponseEntity<>(accountService.saveAccount(account), HttpStatus.CREATED);
    }

    @GetMapping("/account/{id}")
    @Operation(summary = "Retrieve an account by ID")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable final Long id) {
            return new ResponseEntity<>(accountService.getAccount(id), HttpStatus.OK);
    }

    @PutMapping("/accounts/{id}")
    @Operation(summary = "Update an existing account")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable final Long id, @Valid @RequestBody AccountUpdateRequest account) {
        try {
            return new ResponseEntity<>(accountService.updateAccount(id, account), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/accounts/{id}")
    @Operation(summary = "Delete an existing account")
    public ResponseEntity<Void> deleteAccount(@PathVariable final Long id) {
        try {
            accountService.deleteAccount(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
