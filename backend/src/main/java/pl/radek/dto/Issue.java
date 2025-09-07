package pl.radek.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Issue(String filePath,
                    int lineNumber,
                    Issue.Severity severity,
                    String issueDescription,
                    String suggestedAction)
{
    @Override
    public String toString() {
        return String.format("Issue (%s, %s, %s, %s, %s)",
                this.filePath,
                this.lineNumber,
                this.severity,
                this.issueDescription,
                this.suggestedAction
        );
    }
    
    public enum Severity
    {
        CRITICAL,
        MEDIUM,
        LOW,
    }
}