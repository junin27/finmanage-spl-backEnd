package finmanage.core.service;

import finmanage.core.dto.ChartDataDto;
import finmanage.core.dto.FinancialSummaryDto;

import java.time.LocalDate;

public interface ReportService {

    FinancialSummaryDto getFinancialSummary(Long userId, LocalDate startDate, LocalDate endDate);
    ChartDataDto getChartData(Long userId, LocalDate startDate, LocalDate endDate);
    // For export, you might return a byte array, InputStreamResource, or handle HttpServletResponse directly
    byte[] exportTransactionsPdf(Long userId, LocalDate startDate, LocalDate endDate); // Placeholder
    byte[] exportTransactionsCsv(Long userId, LocalDate startDate, LocalDate endDate); // Placeholder

}
