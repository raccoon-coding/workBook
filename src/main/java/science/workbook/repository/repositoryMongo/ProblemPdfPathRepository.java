package science.workbook.repository.repositoryMongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import science.workbook.domain.ProblemPdfPath;

import java.math.BigInteger;

public interface ProblemPdfPathRepository extends MongoRepository<ProblemPdfPath, BigInteger> {
}
