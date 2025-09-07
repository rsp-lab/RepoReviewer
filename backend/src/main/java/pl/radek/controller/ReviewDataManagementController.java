package pl.radek.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.radek.dto.ScanEntry;
import pl.radek.dto.ScanFullReview;
import pl.radek.service.ReviewService;

import java.net.URI;
import java.util.List;

@RestController()
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewDataManagementController
{
    private final String mappingPath = this.getClass().getAnnotation(RequestMapping.class).value()[0];
    
    private final ReviewService reviewService;
    
    @GetMapping()
    public ResponseEntity<List<ScanEntry>> getAllCodeReviews() {
        log.debug("🌐 Handling: GET {}", mappingPath);
        return ResponseEntity.ok(reviewService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ScanFullReview> getCodeReview(@PathVariable String id) {
        log.debug("🌐 Handling: GET {}/{}", mappingPath, id);
        return ResponseEntity.ok(reviewService.findById(id));
    }
    
    @PostMapping()
    public ResponseEntity<ScanFullReview> createCodeReview(@Valid @RequestBody ScanFullReview scan) {
        log.debug("🌐 Handling: POST {} with object: {}", mappingPath, scan);
        ScanFullReview result = reviewService.save(scan);
        return ResponseEntity.created(URI.create("/review/" + result.id())).body(result);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodeReview(@PathVariable String id) {
        log.debug("🌐 Handling: DELETE {}/{}", mappingPath, id);
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
