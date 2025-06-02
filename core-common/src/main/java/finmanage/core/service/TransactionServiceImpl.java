package finmanage.core.service; // Pacote corrigido para 'impl' e 'com.example'

import finmanage.core.dto.TransactionRequestDto;

import finmanage.core.dto.TransactionResponseDto;

import finmanage.core.entity.Category;

import finmanage.core.entity.FinancialAccount;

import finmanage.core.entity.Transaction;

import finmanage.core.entity.User;

import finmanage.core.enums.CategoryType;

import finmanage.core.enums.TransactionType;

import finmanage.core.exception.BadRequestException;

import finmanage.core.exception.ResourceNotFoundException;

import finmanage.core.exception.UnauthorizedOperationException;

import finmanage.core.repository.CategoryRepository;

import finmanage.core.repository.FinancialAccountRepository;

import finmanage.core.repository.TransactionRepository;

import finmanage.core.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;



import java.time.LocalDate;

import java.util.List;

import java.util.stream.Collectors;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository; // Nome padronizado
    private final FinancialAccountRepository financialAccountRepository; // Nome padronizado
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  UserRepository userRepository, // Nome padronizado
                                  FinancialAccountRepository financialAccountRepository, // Nome padronizado
                                  CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository; // Nome padronizado
        this.financialAccountRepository = financialAccountRepository; // Nome padronizado
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto requestDto, Long userId) {
        User user = this.userRepository.findById(userId) // Corrigido: usa 'this.userRepository'
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        FinancialAccount account = this.financialAccountRepository.findByIdAndUserId(requestDto.getFinancialAccountId(), userId) // Corrigido: usa 'this.financialAccountRepository'
                .orElseThrow(() -> new ResourceNotFoundException("Financial Account", "id", requestDto.getFinancialAccountId()));
        Category category = this.categoryRepository.findByIdAndUserId(requestDto.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", requestDto.getCategoryId()));

        TransactionType transactionTypeFromDto = requestDto.getType();
        CategoryType categoryTypeFromEntity = category.getTypeCategory(); // Assumindo que Category.java tem getType()

        boolean typesMismatch = false;
        if (transactionTypeFromDto == TransactionType.INCOME && categoryTypeFromEntity != CategoryType.INCOME) {
            typesMismatch = true;
        } else if (transactionTypeFromDto == TransactionType.EXPENSE && categoryTypeFromEntity != CategoryType.EXPENSE) {
            typesMismatch = true;
        }

        if (typesMismatch) {
            throw new BadRequestException("Transaction type ('" + transactionTypeFromDto + "') does not match category type ('" + categoryTypeFromEntity + "').");
        }

        Transaction transaction = new Transaction();
        transaction.setDescription(requestDto.getDescription());
        transaction.setValue(requestDto.getAmount()); // Corrigido: assumindo setAmount na entidade Transaction
        transaction.setDate(requestDto.getDate());
        transaction.setType(requestDto.getType());
        transaction.setFinancialAccount(account);
        transaction.setCategory(category);
        transaction.setUser(user);

        Transaction savedTransaction = this.transactionRepository.save(transaction);

        return mapToResponseDto(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDto getTransactionById(Long transactionId, Long userId) {
        Transaction transaction = this.transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));
        return mapToResponseDto(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByUserId(Long userId) {
        if (!this.userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return this.transactionRepository.findByUserIdOrderByDateDesc(userId).stream()
                .map(this::mapToResponseDto) // Referência de método correta
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        if (!this.userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return this.transactionRepository.findByUserIdAndPeriod(userId, startDate, endDate).stream()
                .map(this::mapToResponseDto) // Referência de método correta
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByFinancialAccount(Long accountId, Long userId) {
        // Verificação mais segura da conta
        FinancialAccount account = this.financialAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial Account", "id", accountId));
        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException("User not authorized for this financial account.");
        }

        return this.transactionRepository.findByFinancialAccountIdAndUserIdOrderByDateDesc(accountId, userId).stream()
                .map(this::mapToResponseDto) // Referência de método correta
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionResponseDto updateTransaction(Long transactionId, TransactionRequestDto requestDto, Long userId) { // Corrigido: tipo de retorno
        Transaction transaction = this.transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException("User not authorized to update this transaction.");
        }

        // Reutiliza as buscas e validações do createTransaction se possível, ou repete:
        FinancialAccount account = this.financialAccountRepository.findByIdAndUserId(requestDto.getFinancialAccountId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial Account", "id", requestDto.getFinancialAccountId()));
        Category category = this.categoryRepository.findByIdAndUserId(requestDto.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", requestDto.getCategoryId()));

        TransactionType transactionTypeFromDto = requestDto.getType();
        CategoryType categoryTypeFromEntity = category.getTypeCategory();

        boolean typesMismatch = false;
        if (transactionTypeFromDto == TransactionType.INCOME && categoryTypeFromEntity != CategoryType.INCOME) {
            typesMismatch = true;
        } else if (transactionTypeFromDto == TransactionType.EXPENSE && categoryTypeFromEntity != CategoryType.EXPENSE) {
            typesMismatch = true;
        }

        if (typesMismatch) {
            throw new BadRequestException("Transaction type ('" + transactionTypeFromDto + "') does not match category type ('" + categoryTypeFromEntity + "').");
        }

        transaction.setDescription(requestDto.getDescription());
        transaction.setValue(requestDto.getAmount()); // Corrigido: assumindo setAmount na entidade Transaction
        transaction.setDate(requestDto.getDate());
        transaction.setType(requestDto.getType());
        transaction.setFinancialAccount(account);
        transaction.setCategory(category);
        // O usuário da transação não deve mudar

        Transaction updatedTransaction = this.transactionRepository.save(transaction);
        return mapToResponseDto(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction transaction = this.transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));
        if (!transaction.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException("User not authorized to delete this transaction.");
        }
        this.transactionRepository.delete(transaction);
    }

    // --- MÉTODO HELPER CORRIGIDO ---
    private TransactionResponseDto mapToResponseDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setId(transaction.getId());
        dto.setDescription(transaction.getDescription());
        dto.setAmount(transaction.getValue());     // Assumindo getAmount() na entidade Transaction
        dto.setDate(transaction.getDate());
        dto.setType(transaction.getType());         // Assumindo getType() que retorna TransactionType na entidade

        if (transaction.getFinancialAccount() != null) {
            dto.setFinancialAccountId(transaction.getFinancialAccount().getId());
            dto.setFinancialAccountName(transaction.getFinancialAccount().getName());
        }
        if (transaction.getCategory() != null) {
            dto.setCategoryId(transaction.getCategory().getId());
            dto.setCategoryName(transaction.getCategory().getName());
        }
        if (transaction.getUser() != null) {
            dto.setUserId(transaction.getUser().getId());
        }
        dto.setCreationDate(transaction.getDateCreate()); // Assumindo getCreationDate() na entidade
        dto.setUpdateDate(transaction.getDateUpdate());     // Assumindo getUpdateDate() na entidade

        return dto; // Retornando o DTO
    }
}