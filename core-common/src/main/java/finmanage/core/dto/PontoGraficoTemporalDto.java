package finmanage.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate; // Ou String, dependendo de como o frontend prefere

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoGraficoTemporalDto {
    private LocalDate date; // Or String label (e.g., "Jan/25")
    private BigDecimal incomeValue;
    private BigDecimal expenseValue;
}