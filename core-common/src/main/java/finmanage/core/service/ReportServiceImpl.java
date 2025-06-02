package finmanage.core.service;


import finmanage.core.dto.ChartDataDto;
import finmanage.core.dto.DataPointDto;
import finmanage.core.dto.FinancialSummaryDto;
import finmanage.core.entity.Transaction;
import finmanage.core.entity.User;
import finmanage.core.enums.TransactionType;
import finmanage.core.exception.ResourceNotFoundException;
import finmanage.core.repository.TransactionRepository;
import finmanage.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final UserRepository usuarioRepository;

    @Autowired
    public ReportServiceImpl(TransactionRepository transactionRepository, UserRepository usuarioRepository) {
        this.transactionRepository = transactionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialSummaryDto getFinancialSummary(Long userId, LocalDate startDate, LocalDate endDate) {
        if (!usuarioRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        List<Transaction> transactions = transactionRepository.findByUserIdAndPeriod(userId, startDate, endDate);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new FinancialSummaryDto(totalIncome, totalExpenses, totalIncome.subtract(totalExpenses));
    }

    @Override
    @Transactional(readOnly = true)
    public ChartDataDto getChartData(Long userId, LocalDate startDate, LocalDate endDate) {
        if (!usuarioRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        List<Transaction> transactions = transactionRepository.findByUserIdAndPeriod(userId, startDate, endDate);
        ChartDataDto chartData = new ChartDataDto();

        chartData.setExpensesByCategory(
                transactions.stream()
                        .filter(t -> t.getType() == TransactionType.INCOME)
                        .collect(Collectors.groupingBy(
                                t -> t.getCategory().getName(),
                                Collectors.reducing(BigDecimal.ZERO, Transaction::getValue, BigDecimal::add)
                        ))
        );

        chartData.setExpensesByCategory(
                transactions.stream()
                        .filter(t -> t.getType() == TransactionType.EXPENSE)
                        .collect(Collectors.groupingBy(
                                t -> t.getCategory().getName(),
                                Collectors.reducing(BigDecimal.ZERO, Transaction::getValue, BigDecimal::add)
                        ))
        );

        // Example for temporal data (monthly summary)
        // This needs more sophisticated grouping, perhaps by year-month
        Map<LocalDate, List<Transaction>> groupedByDate = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getDate)); // Simplified grouping

        List<DataPointDto> temporalData = groupedByDate.entrySet().stream()
                .map(entry -> {
                    BigDecimal income = entry.getValue().stream()
                            .filter(t -> t.getType() == TransactionType.INCOME)
                            .map(Transaction::getValue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal expense = entry.getValue().stream()
                            .filter(t -> t.getType() == TransactionType.EXPENSE)
                            .map(Transaction::getValue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new DataPointDto(entry.getKey(), income, expense);
                })
                .sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate()))
                .collect(Collectors.toList());
        chartData.setTemporalEvolution(temporalData);

        return chartData;
    }

    @Override
    public byte[] exportTransactionsPdf(Long userId, LocalDate startDate, LocalDate endDate) {
        logger.info("Placeholder: PDF export called for user {}, from {} to {}", userId, startDate, endDate);
        // Implementation using iText or Apache PDFBox would go here.
        // Fetch transactions, create PDF document, and return as byte[].
        String content = "This is a placeholder PDF content for transactions.";
        return content.getBytes(); // Replace with actual PDF bytes
    }

    @Override
    public byte[] exportTransactionsCsv(Long userId, LocalDate startDate, LocalDate endDate) {
        logger.info("Placeholder: CSV export called for user {}, from {} to {}", userId, startDate, endDate);
        // Implementation using Apache Commons CSV or similar would go here.
        // Fetch transactions, format as CSV, and return as byte[].
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("ID,Description,Date,Type,Amount,Category\n");
        // Example line:
        // csvContent.append("1,Salary,2025-01-05,INCOME,5000.00,Salary\n");
        return csvContent.toString().getBytes(); // Replace with actual CSV bytes
    }
}