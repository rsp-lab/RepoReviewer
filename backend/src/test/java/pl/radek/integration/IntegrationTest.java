package pl.radek.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import pl.radek.ai.AbstractAiPlatform;
import pl.radek.dto.ScanFullReview;
import pl.radek.dto.ScanRequestInput;
import pl.radek.service.GitHubService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class IntegrationTest
{
    private RestClient restClient;
    
    @MockitoSpyBean
    private AbstractAiPlatform aiPlatform;
    
    @MockitoBean
    private GitHubService gitHubService;
    
    @LocalServerPort
    private int port;
    
    @BeforeEach
    void setup(@Autowired RestClient.Builder restClientBuilder,
               @TempDir Path tempDir) throws Exception {
        this.restClient = restClientBuilder.baseUrl("http://localhost:" + port).build();
        
        Mockito.doAnswer(invocation -> {
            String repoUrl = invocation.getArgument(0);
            File repoDir = invocation.getArgument(1);
            copyFromTestRepositoryIntoDirectory(repoDir.toPath());
            log.debug("Repository ({}) successfully cloned into directory: {}", repoUrl, tempDir);
            return null;
        }).when(gitHubService).cloneGitRepository(Mockito.anyString(), Mockito.any(File.class));
        
        Mockito.doReturn(mockIssueList1(), mockIssueList2(), mockIssueList3())
                .when(aiPlatform).requestReviewAsJsonString(Mockito.anyString());
    }
    
    @Test
    void shouldExecuteHappyPathSuccessfully_returnsExpectedResponse() throws Exception {
        ScanRequestInput repo = new ScanRequestInput("https://github.com/someUser/someProject", "prompt_per_class");
        ScanFullReview response = sendOrdinaryRestClientRequest(repo);
        
        String jsonActual = convertResponseToJsonString(response);
        String expectedJson = readFileFromPath("src/test/resources/expectedJson.json");
        
        log.debug("Json Actual:\n{}", jsonActual);
        log.debug("Json Expected:\n{}", expectedJson);
        
        JSONAssert.assertEquals(expectedJson, jsonActual, false);
    }
    
    @Test
    void shouldReturnEmptyIssueList_whenAiReturnsNoIssues() throws Exception {
        Mockito.doReturn(mockEmptyIssueList(), mockEmptyIssueList(), mockEmptyIssueList())
                .when(aiPlatform).requestReviewAsJsonString(Mockito.anyString());
        
        ScanRequestInput repo = new ScanRequestInput("https://github.com/prefectUser/perfectProject", null);
        ScanFullReview response = sendOrdinaryRestClientRequest(repo);
        
        String jsonActual = convertResponseToJsonString(response);
        String expectedJson = readFileFromPath("src/test/resources/expectedJsonEmptyList.json");
        
        log.debug("Json Actual:\n{}", jsonActual);
        log.debug("Json Expected:\n{}", expectedJson);
        
        JSONAssert.assertEquals(expectedJson, jsonActual, false);
    }
    
    @Test
    void shouldThrowAiAnalyzeException_whenJsonResponseStructureIsIncorrect() throws Exception {
        Mockito.doReturn(mockWrongIssueList())
                .when(aiPlatform).requestReviewAsJsonString(Mockito.anyString());
        
        ScanRequestInput repo = new ScanRequestInput("https://github.com/someUser/someProject", null);
        
        assertThrows(HttpServerErrorException.ServiceUnavailable.class, () -> {
            sendOrdinaryRestClientRequest(repo);
        });
    }
    
    private ScanFullReview sendOrdinaryRestClientRequest(final ScanRequestInput repo) {
        return restClient.post()
                // .uri(aiProperties.getEndpoint() + "?key=" + aiProperties.getApiKey())
                .uri("/scan")
                .contentType(MediaType.APPLICATION_JSON)
                .body(repo)
                .retrieve()
                .body(ScanFullReview.class);
    }
    
    private String convertResponseToJsonString(final ScanFullReview response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(response);
    }
    
    private String readFileFromPath(final String stringPath) throws IOException {
        Path resource = Paths.get(stringPath);
        return Files.readString(resource);
    }
    
    private void copyFromTestRepositoryIntoDirectory(Path targetDir) throws IOException {
        Path sourceTestRepo = Paths.get("src/test/resources/test-repo");
        
        try (Stream<Path> paths = Files.walk(sourceTestRepo)) {
            for (Path source : (Iterable<Path>) paths::iterator) {
                Path target = targetDir.resolve(sourceTestRepo.relativize(source).toString());
                if (Files.isDirectory(source))
                    Files.createDirectories(target);
                else
                    Files.copy(source, target);
            }
        }
    }
    
    private String mockIssueList1() {
        return """
               {
                 "issues": [
                   {
                     "filePath": "src/main/java/pl/radek/model/BadCalculator.java",
                     "lineNumber": 16,
                     "severity": "CRITICAL",
                     "issueDescription": "The divide method does not handle the case where the divisor is zero",
                     "suggestedAction": "Implement input validation to check if the divisor is zero before performing the division."
                   },
                   {
                     "filePath": "src/main/java/pl/radek/model/BadCalculator.java",
                     "lineNumber": 20,
                     "severity": "LOW",
                     "issueDescription": "The variable 'notNeeded' is instantiated but never used.",
                     "suggestedAction": "Remove the instantiation of 'notNeeded' variable."
                   }
                 ]
               }
               """;
    }
    
    private String mockIssueList2() {
        return """
               {
                 "issues": [
                   {
                     "filePath": "src/main/java/pl/radek/controller/BadController.java",
                     "lineNumber": 23,
                     "severity": "MEDIUM",
                     "issueDescription": "The program attempts to divide 5 by 10, resulting in 0 in integer division.",
                     "suggestedAction": "If the program requires floating-point division, change the return type of the divide method to double and cast the operands to double before division."
                   }
                 ]
               }
               """;
    }
    
    private String mockIssueList3() {
        return """
               {
                 "issues": [
                   {
                     "filePath": "src/main/java/pl/radek/badKotlin.kt",
                     "lineNumber": 5,
                     "severity": "LOW",
                     "issueDescription": "The class name 'SomeBadProjectApplication' suggests this is the main application class.",
                     "suggestedAction": "Rename the class to a more descriptive name."
                   },
                   {
                     "filePath": "src/main/java/pl/radek/badKotlin.kt",
                     "lineNumber": 10,
                     "severity": "MEDIUM",
                     "issueDescription": "The test class is missing assertions.",
                     "suggestedAction": "Add assertions to the 'someTestMethod' to validate the expected behavior."
                   }
                 ]
               }
               """;
    }
    
    private String mockWrongIssueList() {
        return """
               {
                 "issues": [
                   {
                     "lineNumber": 5,
                     "severity": "HIGH",
                     "description": "Wrong severity and description field name!",
                     "suggestedAction": "Should be CRITICAL and issueDescription!"
                   }
                 ]
               }
               """;
    }
    
    private String mockEmptyIssueList() {
        return """
               {
                 "issues": []
               }
               """;
    }
}
