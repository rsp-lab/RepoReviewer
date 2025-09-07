package pl.radek.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "code-reviews")
public record ScanFullReview(@Id String id,
                             @NotNull(message = "Field \"repositoryUrl\" cannot be null!")
                             @Pattern(regexp = "^https://github\\.com/[a-zA-Z0-9-_]+/[a-zA-Z0-9-_]+$", message = "Field \"repositoryUrl\" should have the following format: https://github.com/<user>/<repo>.")
                             String repositoryUrl,
                             String scanDateTimeUtc0,
                             int analyzedFilesCount,
                             int totalIssueCount,
                             long issueCountLow,
                             long issueCountMedium,
                             long issueCountCritical,
                             long codeReviewTimeInMs,
                             BigDecimal totalRepoSizeInKB,
                             String summary,
                             List<Issue> issues)
{
    @Override
    public String toString() {
        return String.format("AnalysisScanReview (%s, %s, %s, filesCount=%s, issueCount=%s, low=%s, medium=%s, critical=%s, reviewTime=%s, repoSize=%s, %s, issues=%s)",
                this.id,
                this.repositoryUrl,
                this.scanDateTimeUtc0,
                this.analyzedFilesCount,
                this.totalIssueCount,
                this.issueCountLow,
                this.issueCountMedium,
                this.issueCountCritical,
                this.codeReviewTimeInMs,
                this.totalRepoSizeInKB,
                this.summary,
                this.issues
        );
    }
}
