package science.workbook.repository.repositoryMongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import science.workbook.domain.Subject;

import java.math.BigInteger;
import java.util.Optional;

public interface SubjectRepository extends MongoRepository<Subject, BigInteger> {
    Optional<Subject> findBySubjectName(String name);
}
