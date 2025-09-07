package pl.radek.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.radek.ai.AiProcessingMode;
import pl.radek.dto.ScanFullReview;
import pl.radek.dto.ScanRequestInput;
import pl.radek.service.CodeAnalysisCoreService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/scan")
@RequiredArgsConstructor
@Slf4j
public class CodeScanController
{
    private final String mappingPath = this.getClass()
            .getAnnotation(RequestMapping.class)
            .value()[0];
    
    private final CodeAnalysisCoreService codeAnalysisCoreService;
    
    @PostMapping()
    public ScanFullReview scanGitHubRepository(@Valid @RequestBody ScanRequestInput scanRequestInput) {
        log.debug("🌐 Handling: POST {} with object: {}", mappingPath, scanRequestInput);
        
        String mode = scanRequestInput.mode();
        AiProcessingMode aiProcessingMode = (mode != null) ? AiProcessingMode.valueOf(mode.toUpperCase()) : AiProcessingMode.ONE_PROMPT;
        
        return codeAnalysisCoreService.scanGitHubRepository(scanRequestInput.repoUrl(), aiProcessingMode);
    }
}
