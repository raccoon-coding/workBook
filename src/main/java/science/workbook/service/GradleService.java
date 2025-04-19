package science.workbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.repository.repositoryValid.GradleRepositoryValid;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradleService {
    private final GradleRepositoryValid repository;

    public List<Gradle> findAllGradle(Subject subject) {
        return repository.findAllGradleBySubject(subject);
    }

    public List<Gradle> findGradles(Subject subject, String startName, String endName) {
        List<Gradle> gradleList = repository.findAllGradleBySubject(subject);
        return gradleList.stream()
                .filter(gradle -> gradle.getGradleName().compareToIgnoreCase(startName) >= 0 &&
                        gradle.getGradleName().compareToIgnoreCase(endName) <= 0)
                .collect(Collectors.toList());
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
