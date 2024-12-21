package science.workbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import science.workbook.exception.service.file.ExistDirectory;
import science.workbook.exception.service.file.NotExistDirectory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileService {
    public void createUserDirectory(String userName) {
        File file = new File(getAbsolutePath() + "/" + userName);
        if(!file.exists()) {
            tryDirectory(file, this::createDirectory);
            return;
        }
        throw new ExistDirectory("디렉토리가 이미 존재합니다.");
    }

    public void deleteUserDirectory(String userName) {
        File file = new File(getAbsolutePath() + "/" + userName);
        if(file.exists()) {
            tryDirectory(file, this::deleteDirectory);
            return;
        }
        throw new NotExistDirectory("디렉토리가 존재하지 않습니다.");
    }

    private void tryDirectory(File file, Consumer<File> fileLogic) {
        fileLogic.accept(file);
    }

    private void createDirectory(File file) {
        boolean isMakeDirectory = file.mkdirs();
        if(isMakeDirectory) {
            log.info("디렉토리 생성 완료");
            return;
        }
        log.info("디렉토리 생성 실패");
    }

    private void deleteDirectory(File file) {
        boolean isDeleteDirectory = file.delete();
        if(isDeleteDirectory) {
            log.info("디렉토리 삭제 완료");
            return;
        }
        log.info("디렉토리 삭제 실패");
    }

    private String getAbsolutePath() {
        Path path = Paths.get("pdf");
        return path.toAbsolutePath().toString();
    }
}
