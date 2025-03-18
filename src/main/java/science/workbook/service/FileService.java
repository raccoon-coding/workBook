package science.workbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import science.workbook.exception.service.file.ExistDirectory;
import science.workbook.exception.service.file.FailSavePDF;
import science.workbook.exception.service.file.NotExistDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static science.workbook.exception.constant.ApiErrorMessage.디렉토리_없음_에러;
import static science.workbook.exception.constant.ApiErrorMessage.디렉토리_존재_에러;
import static science.workbook.exception.message.FileMessage.PDF_저장_실패;
import static science.workbook.service.PdfConstant.CREATE_DIRECTORY;
import static science.workbook.service.PdfConstant.DELETE_DIRECTORY;
import static science.workbook.service.PdfConstant.FAIL_CREATE_DIRECTORY;
import static science.workbook.service.PdfConstant.FAIL_DELETE_DIRECTORY;
import static science.workbook.service.PdfConstant.SLASH;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    @Transactional
    public void createUserDirectory(String userName) {
        File file = new File(getAbsolutePath() + SLASH + userName);
        if(!file.exists()) {
            createDirectory(file);
            return;
        }
        throw new ExistDirectory(디렉토리_존재_에러);
    }

    @Transactional
    public void deleteUserDirectory(String userName) {
        File file = new File(getAbsolutePath() + SLASH + userName);
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
    public void deleteFile() {

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
        boolean isDeleteDirectory = file.delete();
        if(isDeleteDirectory) {
            log.info(DELETE_DIRECTORY);
            return;
        }
        log.info(FAIL_DELETE_DIRECTORY);
    }

    private String getAbsolutePath() {
        Path path = Paths.get(PdfConstant.PDF);
        return path.toAbsolutePath().toString();
    }
}
