package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import science.workbook.dto.toService.CreateLogDtoToService;
import science.workbook.repository.repositoryValid.LogRepositoryValid;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {
    @InjectMocks
    private LogService logService;
    @Mock
    private LogRepositoryValid logRepository;

    @Test
    void createLog() {
        CreateLogDtoToService dto = new CreateLogDtoToService("userId", "/join", "email : admin@gmail.com\n password : 1234", null);
        logService.createLog(dto);
    }
}
