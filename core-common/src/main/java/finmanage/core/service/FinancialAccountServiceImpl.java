package finmanage.core.service;


import finmanage.core.dto.FinancialAccountRequestDto;
import finmanage.core.dto.FinancialAccountResponseDto;
import finmanage.core.entity.FinancialAccount;
import finmanage.core.entity.User;
import finmanage.core.exception.ResourceNotFoundException;
import finmanage.core.exception.UnauthorizedOperationException;
import finmanage.core.repository.FinancialAccountRepository;
import finmanage.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class FinancialAccountServiceImpl implements FinancialAccountService {

    private final FinancialAccountRepository accountRepository;
    private final UserRepository userRepository; // Nome da variável corrigido para consistência

    @Autowired
    public FinancialAccountServiceImpl(FinancialAccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository; // Nome da variável corrigido
    }

    @Override
    @Transactional
    public FinancialAccountResponseDto createFinancialAccount(FinancialAccountRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId) // Uso da variável corrigida
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        FinancialAccount account = new FinancialAccount();
        account.setName(requestDto.getName());
        // Assumindo que a entidade FinancialAccount tem o método setTypeAccount
        account.setTypeAccount(requestDto.getAccountType());
        account.setInitialBalance(requestDto.getInitialBalance());
        account.setUser(user);

        FinancialAccount savedAccount = accountRepository.save(account);
        return mapToResponseDto(savedAccount); // Agora correto porque mapToResponseDto retorna o DTO
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialAccountResponseDto> getFinancialAccountsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) { // Uso da variável corrigida
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return accountRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDto) // Agora correto
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    // Corrigido: O tipo de retorno deve ser FinancialAccountResponseDto para corresponder à interface
    public FinancialAccountResponseDto getFinancialAccountById(Long accountId, Long userId) {
        FinancialAccount account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial Account", "id", accountId + " for user " + userId));
        return mapToResponseDto(account); // Agora correto
    }

    @Override
    @Transactional
    public FinancialAccountResponseDto updateFinancialAccount(Long accountId, FinancialAccountRequestDto requestDto, Long userId) {
        FinancialAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial Account", "id", accountId));

        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException("User not authorized to update this account.");
        }

        account.setName(requestDto.getName());
        // Assumindo que a entidade FinancialAccount tem o método setTypeAccount
        account.setTypeAccount(requestDto.getAccountType());
        // account.setInitialBalance(requestDto.getInitialBalance()); // Geralmente não se atualiza saldo inicial

        FinancialAccount updatedAccount = accountRepository.save(account);
        return mapToResponseDto(updatedAccount); // Agora correto
    }

    @Override
    @Transactional
    public void deleteFinancialAccount(Long accountId, Long userId) {
        FinancialAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial Account", "id", accountId));
        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException("User not authorized to delete this account.");
        }
        accountRepository.delete(account);
    }

    // --- MÉTODO HELPER CORRIGIDO ---
    // Corrigido: O tipo de retorno é FinancialAccountResponseDto
    private FinancialAccountResponseDto mapToResponseDto(FinancialAccount account) {
        if (account == null) {
            return null;
        }
        FinancialAccountResponseDto dto = new FinancialAccountResponseDto();
        dto.setId(account.getId());
        dto.setName(account.getName());
        // Assumindo que a entidade FinancialAccount tem o método getTypeAccount
        dto.setAccountType(account.getTypeAccount());
        dto.setInitialBalance(account.getInitialBalance());
        if (account.getUser() != null) {
            dto.setUserId(account.getUser().getId());
        }
        return dto; // Retornando o DTO corretamente populado
    }
}