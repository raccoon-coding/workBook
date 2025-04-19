package science.workbook.repository.repositoryValid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import science.workbook.domain.Subject;
import science.workbook.exception.repository.NotFoundSubjectByName;
import science.workbook.repository.repositoryMongo.SubjectRepository;

import java.util.List;
import java.util.Optional;

import static science.workbook.exception.constant.ApiErrorMessage.과목_없음_에러;

@Repository
@RequiredArgsConstructor
public class SubjectRepositoryValid {
    private final SubjectRepository repository;

    public void createSubject(Subject createSubject) {
        repository.save(createSubject);
    }

    public List<Subject> findAllSubject() {
        return repository.findAll();
    }

    public void deleteSubject(Subject deleteSubject) {
        repository.delete(deleteSubject);
    }

    public Subject findByName(String subjectName) {
        Optional<Subject> optional = repository.findBySubjectName(subjectName);

        if(optional.isPresent()) {
            return optional.get();
        }

        throw new NotFoundSubjectByName(과목_없음_에러);
    }
}
