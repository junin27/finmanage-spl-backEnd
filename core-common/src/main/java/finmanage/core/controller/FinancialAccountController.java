package finmanage.core.controller;


import finmanage.core.dto.FinancialAccountRequestDto;
import finmanage.core.dto.FinancialAccountResponseDto;
import finmanage.core.entity.User;
import finmanage.core.service.FinancialAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financial-accounts")
public class FinancialAccountController {

    // Corrigido: Deve ser a INTERFACE FinancialAccountService
    private final FinancialAccountService financialAccountService;

    @Autowired
    // Corrigido: O construtor deve injetar FinancialAccountService
    public FinancialAccountController(FinancialAccountService financialAccountService) {
        this.financialAccountService = financialAccountService;
    }

    @PostMapping
    public ResponseEntity<FinancialAccountResponseDto> createFinancialAccount(
            @Valid @RequestBody FinancialAccountRequestDto requestDto,
            @AuthenticationPrincipal User currentUser) {
        // Agora financialAccountService é do tipo FinancialAccountService,
        // e o método createFinancialAccount na interface/impl espera (requestDto, userId)
        FinancialAccountResponseDto createdAccount = financialAccountService.createFinancialAccount(requestDto, currentUser.getId());
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FinancialAccountResponseDto>> getFinancialAccounts(
            @AuthenticationPrincipal User currentUser) {
        // Corrigido: Deve chamar getFinancialAccountsByUserId do serviço
        List<FinancialAccountResponseDto> accounts = financialAccountService.getFinancialAccountsByUserId(currentUser.getId());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<FinancialAccountResponseDto> getFinancialAccountById(
            @PathVariable Long accountId,
            @AuthenticationPrincipal User currentUser) {
        // Agora financialAccountService é do tipo FinancialAccountService,
        // e o método getFinancialAccountById na interface/impl espera (accountId, userId)
        FinancialAccountResponseDto account = financialAccountService.getFinancialAccountById(accountId, currentUser.getId());
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<FinancialAccountResponseDto> updateFinancialAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody FinancialAccountRequestDto requestDto,
            @AuthenticationPrincipal User currentUser) {
        // Agora financialAccountService é do tipo FinancialAccountService,
        // e o método updateFinancialAccount na interface/impl espera (accountId, requestDto, userId)
        FinancialAccountResponseDto updatedAccount = financialAccountService.updateFinancialAccount(accountId, requestDto, currentUser.getId());
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteFinancialAccount(
            @PathVariable Long accountId,
            @AuthenticationPrincipal User currentUser) {
        // Agora financialAccountService é do tipo FinancialAccountService,
        // e o método deleteFinancialAccount na interface/impl espera (accountId, userId)
        financialAccountService.deleteFinancialAccount(accountId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}