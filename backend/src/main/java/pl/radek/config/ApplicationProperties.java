package pl.radek.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class ApplicationProperties
{
    private String baseDir;
    private Set<String> fileExtensionsWhiteList;
}
