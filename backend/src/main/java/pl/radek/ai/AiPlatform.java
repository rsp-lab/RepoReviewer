package pl.radek.ai;

import pl.radek.dto.CodeSummary;

public interface AiPlatform
{
    CodeSummary callAi(String prompt) throws Exception;
}
