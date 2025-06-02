package finmanage.core.controller;


import finmanage.core.dto.TransactionRequestDto;
import finmanage.core.dto.TransactionResponseDto;
import finmanage.core.entity.User;
import finmanage.core.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody TransactionRequestDto requestDto,
            @AuthenticationPrincipal User currentUser) {
        TransactionResponseDto createdTransaction = transactionService.createTransaction(requestDto, currentUser.getId());
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getTransactions(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionResponseDto> transactions;
        if (startDate != null && endDate != null) {
            transactions = transactionService.getTransactionsByUserIdAndPeriod(currentUser.getId(), startDate, endDate);
        } else {
            transactions = transactionService.getTransactionsByUserId(currentUser.getId());
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByAccount(
            @PathVariable Long accountId,
            @AuthenticationPrincipal User currentUser) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByFinancialAccount(accountId, currentUser.getId());
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(
            @PathVariable Long transactionId,
            @AuthenticationPrincipal User currentUser) {
        TransactionResponseDto transaction = transactionService.getTransactionById(transactionId, currentUser.getId());
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long transactionId,
            @Valid @RequestBody TransactionRequestDto requestDto,
            @AuthenticationPrincipal User currentUser) {
        TransactionResponseDto updatedTransaction = transactionService.updateTransaction(transactionId, requestDto, currentUser.getId());
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long transactionId,
            @AuthenticationPrincipal User currentUser) {
        transactionService.deleteTransaction(transactionId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}