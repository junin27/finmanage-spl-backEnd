package finmanage.core.dto;

import finmanage.core.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequestDto {
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Transaction type is required")
    private TransactionType type; // INCOME or EXPENSE

    @NotNull(message = "Financial account ID is required")
    private Long financialAccountId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}