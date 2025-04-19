package science.workbook.repository.repositoryMongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface GradleRepository extends MongoRepository<Gradle, BigInteger> {
    Optional<Gradle> findByGradleName(String name);
    List<Gradle> findBySubject(Subject subject);
    void deleteAll(Iterable<? extends Gradle> gradles);
}
