package pl.radek.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.radek.dto.ScanEntry;
import pl.radek.dto.ScanFullReview;
import pl.radek.exception.NotFoundException;
import pl.radek.repository.ReviewRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService
{
    private final ReviewRepository repository;
    
    public List<ScanEntry> findAll() {
        return repository.findAll()
                .stream()
                .map(scan -> new ScanEntry(scan.id(),
                        scan.repositoryUrl(),
                        scan.scanDateTimeUtc0()
                ))
                .toList();
    }
    
    public ScanFullReview findById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Object not found with id: " + id));
    }
    
    @Cacheable(cacheNames = "repositories", key = "#scan.repositoryUrl()")
    public ScanFullReview save(ScanFullReview scan) {
        if (scan.id() != null)
            return repository.save(scan);
        else {
            log.debug("Received object with empty id. Saving with newly created id.");
            ScanFullReview withId = new ScanFullReview(
                    UUID.randomUUID().toString(),
                    scan.repositoryUrl(),
                    scan.scanDateTimeUtc0(),
                    scan.analyzedFilesCount(),
                    scan.totalIssueCount(),
                    scan.issueCountLow(),
                    scan.issueCountMedium(),
                    scan.issueCountCritical(),
                    scan.codeReviewTimeInMs(),
                    scan.totalRepoSizeInKB(),
                    scan.summary(),
                    scan.issues()
            );
            return repository.save(withId);
        }
    }
    
    @CacheEvict(value = "repositories", key = "#result.repositoryUrl")
    public ScanFullReview deleteById(String id) {
        ScanFullReview review = findById(id);
        repository.deleteById(id);
        log.debug("Successfully deleted object with id: {}", id);
        return review;
    }
}
