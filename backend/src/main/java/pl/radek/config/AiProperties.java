package pl.radek.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ai")
@Data
public class AiProperties
{
    private String apiKey;
    private String endpoint;
    private String model;
    private String issueSchemaJson;
    private AiPrompt prompt = new AiPrompt();
    
    @Data
    public static class AiPrompt {
        private String onePrompt;
        private String promptPerClass;
    }
}
