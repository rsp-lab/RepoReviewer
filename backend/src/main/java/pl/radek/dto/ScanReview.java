package pl.radek.dto;

import java.math.BigDecimal;

public record ScanReview(int fileCount,
                         long lowIssueCount,
                         long mediumIssueCount,
                         long criticalIssueCount,
                         BigDecimal repoSize,
                         CodeSummary codeSummary)
{
}
