package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import science.workbook.exception.service.file.ExistDirectory;
import science.workbook.exception.service.file.NotExistDirectory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @InjectMocks
    private FileService fileService;

    @Test
    void 디렉토리_생성_확인() {
        fileService.createUserDirectory("라쿤");
        assertThatThrownBy(() -> {
            fileService.createUserDirectory("라쿤");
        }).isInstanceOf(ExistDirectory.class);
        fileService.deleteUserDirectory("라쿤");
    }

    @Test
    void 디렉토리_삭제_확인() {
        fileService.createUserDirectory("라쿤");
        fileService.deleteUserDirectory("라쿤");
        assertThatThrownBy(() -> {
            fileService.deleteUserDirectory("라쿤");
        }).isInstanceOf(NotExistDirectory.class);
    }
}
