package finmanage.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataDto {
    // For pie chart of income by category, for example
    private Map<String, BigDecimal> incomeByCategory;
    // For pie chart of expenses by category
    private Map<String, BigDecimal> expensesByCategory;
    // For line/bar chart of Income vs Expenses over time
    private List<DataPointDto> temporalEvolution;
}