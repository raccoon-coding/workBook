package science.workbook.repository.repositoryMongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import science.workbook.domain.Refresh;

import java.math.BigInteger;
import java.util.Optional;

public interface RefreshRepository extends MongoRepository<Refresh, BigInteger> {
    Optional<Refresh> findByEmail(String email);
}
