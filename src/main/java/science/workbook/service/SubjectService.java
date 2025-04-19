package science.workbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.domain.Subject;
import science.workbook.exception.repository.NotFoundSubjectByName;
import science.workbook.repository.repositoryValid.SubjectRepositoryValid;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectService {
    private final SubjectRepositoryValid repository;

    @Transactional
    public void createSubject(String subjectName) {
        Subject createSubject = new Subject(subjectName);
        repository.createSubject(createSubject);
    }

    @Transactional
    public void deleteSubject(String subjectName) {
        Subject deleteSubject = repository.findByName(subjectName);
        repository.deleteSubject(deleteSubject);
    }

    public boolean validSubjectName(String name) {
        try {
            repository.findByName(name);
            return false;
        } catch (NotFoundSubjectByName e) {
            return true;
        }
    }

    public Subject findSubject(String subjectName) {
        return repository.findByName(subjectName);
    }

    public List<Subject> findAllSubjects() {
        return repository.findAllSubject();
    }
}
