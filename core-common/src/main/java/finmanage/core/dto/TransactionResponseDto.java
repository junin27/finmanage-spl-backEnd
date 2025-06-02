package finmanage.core.dto;

import finmanage.core.enums.TransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private TransactionType type;
    private Long financialAccountId;
    private String financialAccountName; // Added for convenience
    private Long categoryId;
    private String categoryName; // Added for convenience
    private Long userId;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}