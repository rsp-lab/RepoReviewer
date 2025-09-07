package pl.radek.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import pl.radek.dto.CodeSummary;

import java.util.Collections;

@Slf4j
public abstract class AbstractAiPlatform implements AiPlatform
{
    @Override
    public final CodeSummary callAi(String prompt) throws Exception {
        String responseContent = requestReviewAsJsonString(prompt);
        return translateResponseToIssueList(responseContent);
    }
    
    public abstract String requestReviewAsJsonString(String prompt) throws Exception;
    
    protected CodeSummary translateResponseToIssueList(String responseContent) throws Exception {
        log.trace("[AI] ResponseBody: {}", responseContent);
        
        if (responseContent.isBlank())
            return new CodeSummary("", Collections.emptyList());
        
        return new ObjectMapper().readValue(responseContent, CodeSummary.class);
    }
}
