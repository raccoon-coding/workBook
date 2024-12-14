package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import science.workbook.dto.toService.CreateLogDto;
import science.workbook.repository.repositoryValid.LogRepositoryValid;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {
    @InjectMocks
    private LogService logService;
    @Mock
    private LogRepositoryValid logRepository;

    @Test
    void createLog() {
        CreateLogDto dto = new CreateLogDto("userId", "/join", "email : admin@gmail.com\n password : 1234", null);
        logService.createLog(dto);
    }
}
