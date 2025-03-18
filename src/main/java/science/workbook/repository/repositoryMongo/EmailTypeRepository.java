package science.workbook.repository.repositoryMongo;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import science.workbook.domain.EmailType;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailTypeRepository extends MongoRepository<EmailType, BigInteger> {
    Optional<EmailType> findByEmail(String email);
    @Query("{ 'createdAt' : { $gte: ?0 } }")
    List<EmailType> findRecentEmailTypes(LocalDateTime since, Sort sort);
}
