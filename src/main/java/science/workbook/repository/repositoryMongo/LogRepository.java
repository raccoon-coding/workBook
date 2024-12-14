package science.workbook.repository.repositoryMongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import science.workbook.domain.Log;

public interface LogRepository extends MongoRepository<Log, Long> {
}
