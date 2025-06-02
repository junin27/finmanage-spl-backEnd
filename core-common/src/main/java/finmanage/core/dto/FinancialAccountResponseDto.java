package finmanage.core.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FinancialAccountResponseDto {
    private Long id;
    private String name;
    private String accountType;
    private BigDecimal initialBalance;
    private Long userId;
}