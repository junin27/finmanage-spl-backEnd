package finmanage.core.service;

import finmanage.core.dto.FinancialAccountRequestDto;
import finmanage.core.dto.FinancialAccountResponseDto;

import java.util.List;

public interface FinancialAccountService {

    FinancialAccountResponseDto createFinancialAccount(FinancialAccountRequestDto requestDto, Long userId);
    List<FinancialAccountResponseDto> getFinancialAccountsByUserId(Long userId);
    FinancialAccountResponseDto getFinancialAccountById(Long accountId, Long userId);
    FinancialAccountResponseDto updateFinancialAccount(Long accountId, FinancialAccountRequestDto requestDto, Long userId);
    void deleteFinancialAccount(Long accountId, Long userId);

}
