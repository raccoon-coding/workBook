package science.workbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.domain.Log;
import science.workbook.dto.response.LogsDto;
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

    public Slice<LogsDto> getLogsAsDto(Pageable pageable) {
        return repository.getPagingLogs(pageable).map(LogsDto::new);
    }
}
