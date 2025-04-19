package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.repository.repositoryValid.GradleRepositoryValid;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradleServiceTest {
    @Mock
    private GradleRepositoryValid repository;

    @InjectMocks
    private GradleService gradleService;

    @Test
    void 과목별_Gradle_조회_확인() {
        Subject subject = new Subject("과학");
        List<Gradle> expected = Arrays.asList(
                new Gradle("gradle1", subject),
                new Gradle("gradle2", subject)
        );

        when(repository.findAllGradleBySubject(subject)).thenReturn(expected);

        List<Gradle> result = gradleService.findAllGradle(subject);

        assertThat(result).isEqualTo(expected);
        verify(repository).findAllGradleBySubject(subject);
    }

    @Test
    void Gradle_추가_확인() {
        Subject subject = new Subject("수학");

        gradleService.addGradle("테스트Gradle", subject);

        ArgumentCaptor<Gradle> captor = ArgumentCaptor.forClass(Gradle.class);
        verify(repository).createGradle(captor.capture());

        Gradle saved = captor.getValue();
        assertThat(saved.getGradleName()).isEqualTo("테스트Gradle");
        assertThat(saved.getSubject()).isEqualTo(subject);
    }

    @Test
    void Gradle_삭제_확인() {
        String gradleName = "삭제할Gradle";
        Gradle gradle = new Gradle(gradleName, new Subject("영어"));

        when(repository.findByName(gradleName)).thenReturn(gradle);

        gradleService.deleteGradle(gradleName);

        verify(repository).findByName(gradleName);
        verify(repository).deleteGradle(gradle);
    }

    @Test
    void Gradle_리스트_삭제_확인() {
        List<Gradle> gradles = Arrays.asList(
                new Gradle("g1", new Subject("음악")),
                new Gradle("g2", new Subject("미술"))
        );

        gradleService.deleteGradles(gradles);

        verify(repository).deleteGradles(gradles);
    }
}
