package finmanage.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FinancialAccountRequestDto {
    @NotBlank(message = "Account name is required")
    private String name;

    private String accountType; // E.g., "WALLET", "CHECKING_ACCOUNT"

    @NotNull(message = "Initial balance is required")
    private BigDecimal initialBalance;
}