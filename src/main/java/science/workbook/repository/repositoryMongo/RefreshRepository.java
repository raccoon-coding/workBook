package science.workbook.repository.repositoryMongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import science.workbook.domain.Refresh;

import java.math.BigInteger;

public interface RefreshRepository extends MongoRepository<Refresh, BigInteger> {
}
