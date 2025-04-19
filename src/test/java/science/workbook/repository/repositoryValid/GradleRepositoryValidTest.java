package science.workbook.repository.repositoryValid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.exception.repository.EmptyGradle;
import science.workbook.exception.repository.NotFoundGradleByName;
import science.workbook.repository.repositoryMongo.GradleRepository;
import science.workbook.repository.repositoryMongo.SubjectRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
class GradleRepositoryValidTest {
    private final GradleRepositoryValid gradleRepository;
    private final SubjectRepositoryValid subjectRepository;

    @Autowired
    public GradleRepositoryValidTest(GradleRepository gradleRepository, SubjectRepository subjectRepository) {
        this.gradleRepository = new GradleRepositoryValid(gradleRepository);
        this.subjectRepository = new SubjectRepositoryValid(subjectRepository);
    }

    @Test
    void Gradle_생성_확인() {
        Subject subject = new Subject("과학");
        subjectRepository.createSubject(subject);

        Gradle gradle = new Gradle("1학년", subject);
        gradleRepository.createGradle(gradle);

        Gradle found = gradleRepository.findByName("1학년");
        assertThat(found.getGradleName()).isEqualTo("1학년");
    }

    @Test
    void Gradle_삭제_확인() {
        Subject subject = new Subject("국어");
        Gradle gradle = new Gradle("2학년", subject);

        subjectRepository.createSubject(subject);
        gradleRepository.createGradle(gradle);

        gradleRepository.deleteGradle(gradle);

        assertThatThrownBy(() -> gradleRepository.findByName("2학년"))
                .isInstanceOf(NotFoundGradleByName.class);
    }

    @Test
    void Gradle_과목별_조회_확인() {
        Subject subject = new Subject("수학");
        Gradle gradle1 = new Gradle("3학년", subject);
        Gradle gradle2 = new Gradle("4학년", subject);

        subjectRepository.createSubject(subject);
        gradleRepository.createGradle(gradle1);
        gradleRepository.createGradle(gradle2);

        List<Gradle> gradles = gradleRepository.findAllGradleBySubject(subject);

        assertThat(gradles).hasSize(2);
    }

    @Test
    void Gradle_과목별_조회_없을_때_예외_확인() {
        Subject subject = new Subject("음악");
        subjectRepository.createSubject(subject);

        assertThatThrownBy(() -> gradleRepository.findAllGradleBySubject(subject))
                .isInstanceOf(EmptyGradle.class);
    }

    @Test
    void Gradle_여러개_삭제_확인() {
        Subject subject = new Subject("미술");
        Gradle gradle1 = new Gradle("5학년", subject);
        Gradle gradle2 = new Gradle("6학년", subject);

        subjectRepository.createSubject(subject);
        gradleRepository.createGradle(gradle1);
        gradleRepository.createGradle(gradle2);

        gradleRepository.deleteGradles(List.of(gradle1, gradle2));

        assertThatThrownBy(() -> gradleRepository.findByName("5학년"))
                .isInstanceOf(NotFoundGradleByName.class);
        assertThatThrownBy(() -> gradleRepository.findByName("6학년"))
                .isInstanceOf(NotFoundGradleByName.class);
    }
}
