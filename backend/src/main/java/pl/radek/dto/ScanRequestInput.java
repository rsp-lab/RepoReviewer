package pl.radek.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ScanRequestInput(
        @NotNull(message = "Field \"repoUrl\" cannot be null!")
        @Pattern(regexp = "^https://github\\.com/[a-zA-Z0-9-_]+/[a-zA-Z0-9-_]+$",
                message = "Field \"repoUrl\" should have the following format: https://github.com/<user>/<repo>.")
        String repoUrl,
        @Pattern(regexp = "|prompt_per_class|one_prompt",
                message = "Field \"mode\" can be empty or accepts only values: prompt_per_class, one_prompt")
        String mode)
{
    @Override
    public String toString() {
        return String.format("GitScanInput (%s, %s)", this.repoUrl, this.mode);
    }
}
