package finmanage.core.service;

import finmanage.core.dto.TransactionRequestDto;
import finmanage.core.dto.TransactionResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    TransactionResponseDto createTransaction(TransactionRequestDto requestDto, Long userId);
    TransactionResponseDto getTransactionById(Long transactionId, Long userId);
    List<TransactionResponseDto> getTransactionsByUserId(Long userId);
    List<TransactionResponseDto> getTransactionsByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);
    List<TransactionResponseDto> getTransactionsByFinancialAccount(Long accountId, Long userId);
    TransactionResponseDto updateTransaction(Long transactionId, TransactionRequestDto requestDto, Long userId);
    void deleteTransaction(Long transactionId, Long userId);

}
