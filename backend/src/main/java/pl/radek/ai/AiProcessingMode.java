package pl.radek.ai;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AiProcessingMode
{
    PROMPT_PER_CLASS,
    ONE_PROMPT;
    
    public static Set<String> getAccessibleModes() {
        return Stream.of(AiProcessingMode.values())
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
