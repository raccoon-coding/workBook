package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import science.workbook.exception.service.file.ExistDirectory;
import science.workbook.exception.service.file.NotExistDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    @Test
    void PDF_생성_확인() throws IOException {
        // given
        String userName = "라쿤";
        String testFileName = "test.pdf";
        byte[] content = "This is a test PDF content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", testFileName, "application/pdf", content);
        fileService.createUserDirectory("라쿤");

        // when
        String filePath = fileService.saveFile(mockFile, userName);

        // then
        File savedFile = new File(filePath);
        assertTrue(savedFile.exists(), "The file should be saved successfully.");
        assertTrue(filePath.contains(userName), "The file path should include the user name.");
        assertEquals(new String(Files.readAllBytes(savedFile.toPath())), new String(content), "The file content should match.");

        savedFile.delete();
        fileService.deleteUserDirectory("라쿤");
    }

    @Test
    void PDF_삭제_확인() throws IOException {
        // given
        String userName = "라쿤";
        String testFileName = "test.pdf";
        byte[] content = "This is a test PDF content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", testFileName, "application/pdf", content);
        fileService.createUserDirectory("라쿤");

        // when
        String filePath = fileService.saveFile(mockFile, userName);
        fileService.deleteFile(userName, testFileName);

        // then
        File savedFile = new File(filePath);
        assertFalse(savedFile.exists(), "The file should be saved successfully.");
        fileService.deleteUserDirectory("라쿤");
    }
}
