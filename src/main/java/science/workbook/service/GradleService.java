package science.workbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.repository.repositoryValid.GradleRepositoryValid;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradleService {
    private final GradleRepositoryValid repository;

    public List<Gradle> findAllGradle(Subject subject) {
        return repository.findAllGradleBySubject(subject);
    }

    @Transactional
    public void addGradle(String name, Subject subject) {
        Gradle createGradle = new Gradle(name, subject);
        repository.createGradle(createGradle);
    }

    @Transactional
    public void deleteGradle(String name) {
        Gradle deleteGradle = repository.findByName(name);
        repository.deleteGradle(deleteGradle);
    }

    @Transactional
    public void deleteGradles(List<Gradle> gradles) {
        repository.deleteGradles(gradles);
    }
}
