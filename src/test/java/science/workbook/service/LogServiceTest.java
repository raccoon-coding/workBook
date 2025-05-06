package science.workbook.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import science.workbook.domain.Log;
import science.workbook.dto.response.LogsDto;
import science.workbook.dto.toService.CreateLogDtoToService;
import science.workbook.repository.repositoryValid.LogRepositoryValid;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {
    @InjectMocks
    private LogService logService;
    @Mock
    private LogRepositoryValid logRepository;

    @Test
    @DisplayName("로그 생성 테스트")
    void createLog() {
        CreateLogDtoToService dto = new CreateLogDtoToService("userId", "/join", "email : admin@gmail.com\n password : 1234", null);
        logService.createLog(dto);
    }

    @Test
    @DisplayName("로그 리스트 확인 테스트")
    void getLogs() {
        List<Log> fakeLogs = createLogs();
        Pageable pageable = PageRequest.of(1, 20);

        Slice<Log> slice = new SliceImpl<>(fakeLogs, pageable, true);

        when(logRepository.getPagingLogs(pageable))
                .thenReturn(slice);

        Slice<LogsDto> dto = logService.getLogsAsDto(pageable);

        assertTrue(dto.getContent().stream()
                .allMatch(log -> log.getRequestUrl().equals("/join")));
    }

    List<Log> createLogs() {
        List<Log> fakeLogs = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            String userId = "userId" + i;
            String url = "/join";
            String body = "email : admin" + i + "@gmail.com\n password : 1234";
            CreateLogDtoToService dto = new CreateLogDtoToService(userId, url, body, null);

            Log log = new Log(dto);
            fakeLogs.add(log);

            logService.createLog(dto);
        }
        return fakeLogs;
    }
}
