package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import science.workbook.domain.Subject;
import science.workbook.exception.repository.NotFoundSubjectByName;
import science.workbook.repository.repositoryValid.SubjectRepositoryValid;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static science.workbook.exception.constant.ApiErrorMessage.과목_없음_에러;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {
    @InjectMocks
    private SubjectService subjectService;

    @Mock
    private SubjectRepositoryValid repository;

    @Test
    void 과목_생성_확인() {
        String subjectName = "수학";

        subjectService.createSubject(subjectName);

        ArgumentCaptor<Subject> captor = ArgumentCaptor.forClass(Subject.class);
        verify(repository).createSubject(captor.capture());

        assertEquals(subjectName, captor.getValue().getSubjectName());
    }

    @Test
    void 과목_삭제_확인() {
        String subjectName = "과학";
        Subject subject = new Subject(subjectName);

        when(repository.findByName(subjectName)).thenReturn(subject);

        subjectService.deleteSubject(subjectName);

        verify(repository).deleteSubject(subject);
    }

    @Test
    void 과목_중복_확인_성공() {
        String subjectName = "영어";

        when(repository.findByName(subjectName)).thenReturn(new Subject(subjectName));

        boolean result = subjectService.validSubjectName(subjectName);

        assertFalse(result);
    }

    @Test
    void 과목_중복_확인_실패() {
        String subjectName = "음악";

        when(repository.findByName(subjectName)).thenThrow(new NotFoundSubjectByName(과목_없음_에러));

        boolean result = subjectService.validSubjectName(subjectName);

        assertTrue(result);
    }

    @Test
    void 과목_단건_조회_확인() {
        String subjectName = "국어";
        Subject subject = new Subject(subjectName);

        when(repository.findByName(subjectName)).thenReturn(subject);

        Subject result = subjectService.findSubject(subjectName);

        assertEquals(subjectName, result.getSubjectName());
    }

    @Test
    void 전체_과목_조회_확인() {
        List<Subject> subjects = List.of(
                new Subject("국어"),
                new Subject("수학")
        );

        when(repository.findAllSubject()).thenReturn(subjects);

        List<Subject> result = subjectService.findAllSubjects();

        assertEquals(2, result.size());
        assertEquals("국어", result.get(0).getSubjectName());
        assertEquals("수학", result.get(1).getSubjectName());
    }
}
