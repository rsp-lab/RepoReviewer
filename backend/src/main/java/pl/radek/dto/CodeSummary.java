package pl.radek.dto;

import java.util.List;

public record CodeSummary(String summary,
                          List<Issue> issues)
{
}