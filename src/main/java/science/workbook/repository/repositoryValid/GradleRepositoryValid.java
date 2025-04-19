package science.workbook.repository.repositoryValid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.exception.repository.EmptyGradle;
import science.workbook.exception.repository.NotFoundGradleByName;
import science.workbook.repository.repositoryMongo.GradleRepository;

import java.util.List;
import java.util.Optional;

import static science.workbook.exception.constant.ApiErrorMessage.학년_없음_에러;

@Repository
@RequiredArgsConstructor
public class GradleRepositoryValid {
    private final GradleRepository repository;

    public void createGradle(Gradle createGradle) {
        repository.save(createGradle);
    }

    public List<Gradle> findAll() {
        return repository.findAll();
    }

    public void deleteGradle(Gradle deleteGradle) {
        repository.delete(deleteGradle);
    }

    public Gradle findByName(String gradleName) {
        Optional<Gradle> optional = repository.findByGradleName(gradleName);

        if(optional.isPresent()) {
            return optional.get();
        }

        throw new NotFoundGradleByName(학년_없음_에러);
    }

    public List<Gradle> findAllGradleBySubject(Subject subject) {
        List<Gradle> gradle = repository.findBySubject(subject);

        if(gradle.isEmpty()) {
            throw new EmptyGradle(학년_없음_에러);
        }

        return gradle;
    }

    public void deleteGradles(List<Gradle> gradles) {
        repository.deleteAll(gradles);
    }
}
