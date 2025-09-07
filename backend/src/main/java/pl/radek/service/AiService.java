package pl.radek.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.radek.ai.AiPlatform;
import pl.radek.config.AiProperties;
import pl.radek.dto.CodeSummary;
import pl.radek.exception.AiAnalyzeException;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService
{
    private final AiPlatform aiPlatform;
    private final AiProperties aiProperties;
    private final FileSystemService fileSystemService;
    
    public CodeSummary analyzeFile(File file) {
        String singlePrompt = aiProperties.getPrompt().getPromptPerClass() + "Code:\n" + fileSystemService.readFileAsString(file);
        return executeAiCodeReview(singlePrompt);
    }
    
    public CodeSummary analyzeFiles(List<File> files) {
        String allFilesContent = files.stream()
                .map(file -> {
                    String classPath = extractClassPath(file);
                    String content = fileSystemService.readFileAsString(file);
                    String filePath = "FilePath \"" + classPath + "\":\n" + content;
                    return filePath;
                })
                .collect(Collectors.joining("\n"));
        String mergedPrompt = aiProperties.getPrompt().getOnePrompt() + "All classes:\n" + allFilesContent;
        
        return executeAiCodeReview(mergedPrompt);
    }
    
    private CodeSummary executeAiCodeReview(String prompt) {
        try {
            CodeSummary codeSummary = aiPlatform.callAi(prompt);
            logSummary(codeSummary);
            return codeSummary;
        }
        catch (Exception e) {
            throw new AiAnalyzeException(String.format("🚫 AI Exception: %s", e.getMessage()));
        }
    }
    
    private String extractClassPath(File file) {
        // Remove repositories\repo-uuid\ always from file path
        Path filePath = file.toPath();
        int nameCount = filePath.getNameCount();
        
        String fullPath;
        if (nameCount > 2) {
            Path relativePath = filePath.subpath(2, nameCount);
            fullPath = relativePath.toString();
        }
        else
            fullPath = filePath.toString();
        
        int index = fullPath.indexOf("src");
        return (index != -1) ? fullPath.substring(index) : fullPath;
    }
    
    private void logSummary(CodeSummary codeSummary) {
        log.debug("📋 Summary: {}", codeSummary.summary());
        codeSummary.issues().forEach(issue -> log.debug("⚠️ Found issue: {}", issue));
    }
}
