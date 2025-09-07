package pl.radek.dto;

import org.springframework.data.annotation.Id;

public record ScanEntry(@Id String id,
                        String repositoryUrl,
                        String scanDateTimeUtc0)
{
}
