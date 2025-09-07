package pl.radek.ai;

import com.google.genai.Client;
import com.google.genai.errors.ClientException;
import com.google.genai.errors.ServerException;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.radek.config.AiProperties;
import pl.radek.exception.AiAnalyzeException;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleGeminiAi extends AbstractAiPlatform
{
    private final AiProperties aiProperties;
    
    @Override
    public String requestReviewAsJsonString(final String prompt) {
        final String apiKey = aiProperties.getApiKey();
        final String model = aiProperties.getModel();
        final String issueSchemaJson = aiProperties.getIssueSchemaJson();
        
        try (Client client = Client.builder().apiKey(apiKey).build()) {
            
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseMimeType("application/json")
                    .candidateCount(1) // One issue list is enough
                    .responseSchema(Schema.fromJson(issueSchemaJson))
                    .build();
            
            GenerateContentResponse response = client.models.generateContent(model, prompt, config);
            
            return response.text();
        }
        catch (ServerException e) {
            log.warn("Google API is currently overloaded! {}", e.getMessage());
            return "";
        }
        catch (ClientException e) {
            log.warn("Google API limit reached: {}", e.getMessage());
            String message = "Application limit reached. Try again with different mode.";
            throw new AiAnalyzeException(message);
        }
    }
}
