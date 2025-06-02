package finmanage.core.controller;


import finmanage.core.dto.ChartDataDto;
import finmanage.core.dto.FinancialSummaryDto;
import finmanage.core.entity.User;
import finmanage.core.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    public ResponseEntity<FinancialSummaryDto> getFinancialSummary(
            @AuthenticationPrincipal User currentUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        FinancialSummaryDto summary = reportService.getFinancialSummary(currentUser.getId(), startDate, endDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/chart-data")
    public ResponseEntity<ChartDataDto> getChartData(
            @AuthenticationPrincipal User currentUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ChartDataDto chartData = reportService.getChartData(currentUser.getId(), startDate, endDate);
        return ResponseEntity.ok(chartData);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @AuthenticationPrincipal User currentUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] pdfContents = reportService.exportTransactionsPdf(currentUser.getId(), startDate, endDate);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"transactions_report.pdf\"")
                .body(pdfContents);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @AuthenticationPrincipal User currentUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] csvContents = reportService.exportTransactionsCsv(currentUser.getId(), startDate, endDate);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"transactions_report.csv\"")
                .body(csvContents);
    }
}