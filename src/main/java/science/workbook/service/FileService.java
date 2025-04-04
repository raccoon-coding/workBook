package science.workbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import science.workbook.exception.service.file.ExistDirectory;
import science.workbook.exception.service.file.FailSavePDF;
import science.workbook.exception.service.file.NotExistDirectory;
import science.workbook.service.constant.PdfConstant;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static science.workbook.exception.constant.ApiErrorMessage.디렉토리_없음_에러;
import static science.workbook.exception.constant.ApiErrorMessage.디렉토리_존재_에러;
import static science.workbook.exception.message.FileMessage.PDF_저장_실패;
import static science.workbook.service.constant.PdfConstant.CREATE_DIRECTORY;
import static science.workbook.service.constant.PdfConstant.DELETE_DIRECTORY;
import static science.workbook.service.constant.PdfConstant.FAIL_CREATE_DIRECTORY;
import static science.workbook.service.constant.PdfConstant.FAIL_DELETE_DIRECTORY;

@Slf4j @Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    @Transactional
    public void createUserDirectory(String userName) {
        File file = new File(getAbsolutePath() + File.separator + userName);
        if(!file.exists()) {
            createDirectory(file);
            return;
        }
        throw new ExistDirectory(디렉토리_존재_에러);
    }

    @Transactional
    public void deleteUserDirectory(String userName) {
        File file = new File(getAbsolutePath() + File.separator + userName);
        if(file.exists()) {
            deleteDirectory(file);
            return;
        }
        throw new NotExistDirectory(디렉토리_없음_에러);
    }

    @Transactional
    public String saveFile(MultipartFile file, String userName) {
        try {
            String filePath = getAbsolutePath() + File.separator + userName + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            log.info("PDF 저장 완료");
            return filePath;
        } catch (IOException e) {
            throw new FailSavePDF(PDF_저장_실패);
        }
    }

    @Transactional
    public void deleteFile(String userName, String fileName) {
        File file = new File(getAbsolutePath() + File.separator + userName + File.separator + fileName);
        if(file.exists()) {
            deleteDirectory(file);
            return;
        }
        throw new NotExistDirectory(디렉토리_없음_에러);
    }

    private void createDirectory(File file) {
        boolean isMakeDirectory = file.mkdirs();
        if(isMakeDirectory) {
            log.info(CREATE_DIRECTORY);
            return;
        }
        log.info(FAIL_CREATE_DIRECTORY);
    }

    private void deleteDirectory(File file) {
        Path directory = file.toPath();
        if (!Files.exists(directory)) {
            log.info(FAIL_DELETE_DIRECTORY);
            return;
        }

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            log.info(DELETE_DIRECTORY);
        } catch (IOException e) {
            log.error(FAIL_DELETE_DIRECTORY, e);
        }
        log.info(FAIL_DELETE_DIRECTORY);
    }

    private String getAbsolutePath() {
        Path path = Paths.get(PdfConstant.PDF);
        return path.toAbsolutePath().toString();
    }
}
