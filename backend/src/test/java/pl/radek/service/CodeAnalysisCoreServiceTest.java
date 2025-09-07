package pl.radek.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.radek.ai.AiProcessingMode;
import pl.radek.dto.CodeSummary;
import pl.radek.dto.ScanFullReview;
import pl.radek.dto.Issue;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CodeAnalysisCoreServiceTest
{
    public static final String REPOSITORY_PATH = "/repository/repo-fake";
    GitHubService gitHubService;
    FileSystemService fileSystemService;
    AiService aiService;
    ReviewService reviewService;
    
    CodeAnalysisCoreService service;
    
    @BeforeEach
    void setUp() {
        fileSystemService = mock(FileSystemService.class);
        gitHubService = mock(GitHubService.class);
        aiService = mock(AiService.class);
        reviewService = mock(ReviewService.class);
        
        service = new CodeAnalysisCoreService(gitHubService, fileSystemService, aiService, reviewService);
        
        mockFileSystemServiceMethods(new File(REPOSITORY_PATH));
        mockAiServiceMethods();
        mockReviewServiceMethods();
    }
    
    @Test
    void shouldReturnAnalysisScanResponse_withCorrectSummary() {
        String repoUrl = "https://github.com/test/repo";
        AiProcessingMode mode = AiProcessingMode.PROMPT_PER_CLASS;
        
        ScanFullReview response = service.scanGitHubRepository(repoUrl, mode);
        
        assertAll("Response check",
                () -> assertEquals(2, response.analyzedFilesCount()),
                () -> assertEquals(4, response.totalIssueCount()),
                () -> assertEquals(1, response.issueCountLow()),
                () -> assertEquals(2, response.issueCountMedium()),
                () -> assertEquals(1, response.issueCountCritical()),
                () -> assertEquals(new BigDecimal("123.45"), response.totalRepoSizeInKB()),
                () -> assertNotNull(response.issues()),
                () -> assertEquals(Issue.Severity.LOW, response.issues().get(0).severity()),
                () -> assertEquals(Issue.Severity.MEDIUM, response.issues().get(1).severity())
        );
        
        assertAll("Mock interactions",
                () -> verify(gitHubService).cloneGitRepository(repoUrl, new File(REPOSITORY_PATH)),
                () -> verify(fileSystemService).deleteDirectory(new File(REPOSITORY_PATH))
        );
    }
    
    private void mockFileSystemServiceMethods(final File fakeRepoDir) {
        when(fileSystemService.createTemporaryDirectory()).thenReturn(fakeRepoDir);
        when(fileSystemService.calculateDirectorySize(fakeRepoDir)).thenReturn(new BigDecimal(
                "123.45"));
        List<File> javaFiles = List.of(new File("File1.java"), new File("File2.java"));
        when(fileSystemService.findFilesInDirectory(fakeRepoDir)).thenReturn(javaFiles);
    }
    
    private void mockAiServiceMethods() {
        Issue lowIssue = new Issue("somePath1", 1, Issue.Severity.LOW, "Issue 1", "Action 1");
        Issue mediumIssue = new Issue("somePath2", 2, Issue.Severity.MEDIUM, "Issue 2", "Action 2");
        Issue criticalIssue = new Issue("somePath3", 3, Issue.Severity.CRITICAL, "Issue 3", "Action 3");
        
        when(aiService.analyzeFile(any(File.class))).thenAnswer(invocation -> {
            File f = invocation.getArgument(0);
            
            if (f.getName().equals("File1.java"))
                return new CodeSummary("some summary", List.of(lowIssue, mediumIssue));
            else if (f.getName().equals("File2.java"))
                return new CodeSummary("some summary", List.of(mediumIssue, criticalIssue));
            else
                return new CodeSummary("some summary", Collections.emptyList());
        });
    }
    
    private void mockReviewServiceMethods() {
        Mockito.doAnswer(invocation -> invocation.<ScanFullReview>getArgument(0))
                .when(reviewService).save(Mockito.any(ScanFullReview.class));
    }
}