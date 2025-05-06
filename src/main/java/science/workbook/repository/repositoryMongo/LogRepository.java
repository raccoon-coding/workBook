package science.workbook.repository.repositoryMongo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import science.workbook.domain.Log;

import java.math.BigInteger;

public interface LogRepository extends MongoRepository<Log, BigInteger> {
    Slice<Log> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
