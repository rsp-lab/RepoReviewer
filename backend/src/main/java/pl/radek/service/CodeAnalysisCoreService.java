package pl.radek.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.radek.ai.AiProcessingMode;
import pl.radek.dto.CodeSummary;
import pl.radek.dto.ScanFullReview;
import pl.radek.dto.ScanReview;
import pl.radek.dto.Issue;

import java.io.File;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeAnalysisCoreService
{
    private final GitHubService gitHubService;
    private final FileSystemService fileSystemService;
    private final AiService aiService;
    private final ReviewService reviewService;
    
    @Cacheable(cacheNames = "repositories", key = "#repoUrl")
    public ScanFullReview scanGitHubRepository(final String repoUrl, final AiProcessingMode mode) {
        Instant start = Instant.now();
        ScanReview result = executeCodeReviewForRepository(repoUrl, mode);
        Instant end = Instant.now();
        
        long reviewTime = Duration.between(start, end).toMillis();
        log.info("🔎 Scan result:");
        log.info("\t ➡️ Files analyzed: {}", result.fileCount());
        log.info("\t ➡️ Total issues detected: {}", result.codeSummary().issues().size());
        log.info("\t ➡️ Total LOW issues: {}", result.lowIssueCount());
        log.info("\t ➡️ Total MEDIUM issues: {}", result.mediumIssueCount());
        log.info("\t ➡️ Total CRITICAL issues: {}", result.criticalIssueCount());
        log.info("\t ➡️ Review time taken: {} ms", reviewTime);
        log.info("\t ➡️ Repository size: {} KB", result.repoSize());
        
        return reviewService.save(new ScanFullReview(
                UUID.randomUUID().toString(),
                repoUrl,
                OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm 'UTC'x")),
                result.fileCount(),
                result.codeSummary().issues().size(),
                result.lowIssueCount(),
                result.mediumIssueCount(),
                result.criticalIssueCount(),
                reviewTime,
                result.repoSize(),
                result.codeSummary().summary(),
                result.codeSummary().issues()
        ));
    }
    
    private ScanReview executeCodeReviewForRepository(final String repoUrl, final AiProcessingMode mode) {
        File repoDirectory = cloneRepositoryIntoTemporaryDirectory(repoUrl);
        try {
            BigDecimal repoSize = fileSystemService.calculateDirectorySize(repoDirectory);
            List<File> files = fileSystemService.findFilesInDirectory(repoDirectory);
            CodeSummary codeSummary = analyzeFilesForIssues(files, mode);
            return summarizeReviewSeverities(files.size(), repoSize, codeSummary);
        }
        finally {
            fileSystemService.deleteDirectory(repoDirectory);
        }
    }
    
    private File cloneRepositoryIntoTemporaryDirectory(final String repoUrl) {
        File repoDirectory = fileSystemService.createTemporaryDirectory();
        gitHubService.cloneGitRepository(repoUrl, repoDirectory);
        return repoDirectory;
    }
    
    private CodeSummary analyzeFilesForIssues(final List<File> files, AiProcessingMode mode) {
        return switch (mode) {
            case ONE_PROMPT -> aiService.analyzeFiles(files);
            case PROMPT_PER_CLASS -> new CodeSummary("",
                            files.parallelStream()
                            .peek(file -> log.debug("Analyzing file: {}", file.getName()))
                            .map(file -> aiService.analyzeFile(file).issues())
                            .flatMap(List::stream)
                            .toList());
        };
    }
    
    private ScanReview summarizeReviewSeverities(final int fileCount,
                                                 final BigDecimal repoSize,
                                                 final CodeSummary codeSummary) {
        long low = 0;
        long medium = 0;
        long critical = 0;
        for (Issue issue : codeSummary.issues()) {
            switch (issue.severity()) {
                case LOW -> low++;
                case MEDIUM -> medium++;
                case CRITICAL -> critical++;
            }
        }
        
        return new ScanReview(fileCount, low, medium, critical, repoSize, codeSummary);
    }
}
