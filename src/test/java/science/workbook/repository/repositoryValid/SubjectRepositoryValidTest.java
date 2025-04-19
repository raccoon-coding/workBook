package science.workbook.repository.repositoryValid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import science.workbook.domain.Subject;
import science.workbook.exception.repository.NotFoundSubjectByName;
import science.workbook.repository.repositoryMongo.SubjectRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class SubjectRepositoryValidTest {

    private final SubjectRepositoryValid repository;

    @Autowired
    public SubjectRepositoryValidTest(SubjectRepository repository) {
        this.repository = new SubjectRepositoryValid(repository);
    }

    @Test
    void 과목_생성_확인() {
        Subject subject = new Subject("미술");
        repository.createSubject(subject);

        Subject findSubject = repository.findByName("수학");
        assertEquals("수학", findSubject.getSubjectName());
    }

    @Test
    void 과목_전체_조회_확인() {
        repository.createSubject(new Subject("수학"));
        repository.createSubject(new Subject("과학"));

        List<Subject> subjectList = repository.findAllSubject();
        assertThat(subjectList).hasSize(2);
    }

    @Test
    void 과목_삭제_확인() {
        Subject subject = new Subject("역사");
        repository.createSubject(subject);

        repository.deleteSubject(subject);

        assertThatThrownBy(() -> repository.findByName("역사"))
                .isInstanceOf(NotFoundSubjectByName.class);
    }

    @Test
    void 존재하지_않는_과목_조회_예외_확인() {
        assertThatThrownBy(() -> repository.findByName("존재하지않는과목"))
                .isInstanceOf(NotFoundSubjectByName.class);
    }
}
