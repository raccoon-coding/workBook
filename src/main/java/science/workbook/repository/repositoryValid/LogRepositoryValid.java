package science.workbook.repository.repositoryValid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import science.workbook.domain.Log;
import science.workbook.repository.repositoryMongo.LogRepository;

@Repository
@RequiredArgsConstructor
public class LogRepositoryValid {
    private final LogRepository repository;

    public void createLog(Log log) {
        repository.save(log);
    }

    public Slice<Log> getPagingLogs(Pageable pageable) {
        return repository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
