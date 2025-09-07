package pl.radek.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.radek.dto.ScanFullReview;

public interface ReviewRepository extends MongoRepository<ScanFullReview, String>
{
}
