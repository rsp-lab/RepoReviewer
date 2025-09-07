package pl.radek;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import pl.radek.ai.AiProcessingMode;
import pl.radek.config.AiProperties;
import pl.radek.dto.ScanFullReview;
import pl.radek.service.ReviewService;

@SpringBootApplication
@RequiredArgsConstructor
@EnableCaching
@Slf4j
public class Main
{
    private final AiProperties aiProperties;
    
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
    
    @Bean
    CommandLineRunner runAtStartup(ReviewService reviewService) {
        return args -> {
            if (aiProperties.getApiKey() == null || aiProperties.getApiKey().isBlank())
                throw new IllegalStateException("AI API KEY cannot be null!");
            else
                log.debug("Your AI API KEY is: {}", aiProperties.getApiKey());
            
            log.debug("Available working AI modes:");
            AiProcessingMode.getAccessibleModes().forEach(mode -> log.debug("\u001B[32mⓂ️ {}\u001B[0m", mode));
            
            ScanFullReview sampleData = SampleMongoData.getSampleData();
            reviewService.save(sampleData);
            log.debug("Loaded sample review into database with id: {}.", sampleData.id());
        };
    }
}
