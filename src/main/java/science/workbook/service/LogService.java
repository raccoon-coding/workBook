package science.workbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.domain.Log;
import science.workbook.dto.toService.CreateLogDtoToService;
import science.workbook.repository.repositoryValid.LogRepositoryValid;

@Service
@Transactional
@RequiredArgsConstructor
public class LogService {
    private final LogRepositoryValid repository;

    public void createLog(CreateLogDtoToService dto) {
        Log log = new Log(dto);
        repository.createLog(log);
    }
}
