package science.workbook.repository.repositoryValid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import science.workbook.domain.Log;
import science.workbook.dto.toService.CreateLogDto;
import science.workbook.repository.repositoryMongo.LogRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class LogRepositoryValidTest {
    private final LogRepositoryValid repository;

    @Autowired
    public LogRepositoryValidTest(LogRepository repository) {
        this.repository = new LogRepositoryValid(repository);
    }

    @Test
    void 로그_생성_확인() {
        CreateLogDto dto = new CreateLogDto("userId", "/join", "email : admin@gmail.com\n password : 1234", null);
        Log log = new Log(dto);
        repository.createLog(log);
    }
}
