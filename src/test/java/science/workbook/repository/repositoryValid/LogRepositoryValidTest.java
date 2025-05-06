package science.workbook.repository.repositoryValid;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import science.workbook.domain.Log;
import science.workbook.dto.toService.CreateLogDtoToService;
import science.workbook.repository.repositoryMongo.LogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class LogRepositoryValidTest {
    private final LogRepositoryValid repository;

    @Autowired
    public LogRepositoryValidTest(LogRepository repository) {
        this.repository = new LogRepositoryValid(repository);
    }

    @Test
    @DisplayName("로그 생성 테스트")
    void createLog() {
        CreateLogDtoToService dto = new CreateLogDtoToService("userId", "/join", "email : admin@gmail.com\n password : 1234", null);
        Log log = new Log(dto);
        repository.createLog(log);
    }

    @Test
    @DisplayName("로그 리스트 테스트")
    void getLogsInDB() {
        createLogs();
        Pageable pageable = PageRequest.of(0, 20);
        Slice<Log> logs = repository.getPagingLogs(pageable);

        assertThat(logs.getContent().stream()
                .allMatch(i -> i.getUserId().contains("userId")))
                .isTrue();
    }

    void createLogs() {
        for (int i = 1; i <= 50; i++) {
            String userId = "userId" + i;
            String url = "/join";
            String body = "email : admin" + i + "@gmail.com\n password : 1234";
            CreateLogDtoToService dto = new CreateLogDtoToService(userId, url, body, null);

            Log log = new Log(dto);

            repository.createLog(log);
        }
    }
}
