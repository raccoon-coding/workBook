package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import science.workbook.dto.request.GetNewUserDto;
import science.workbook.repository.repositoryValid.UserRepositoryValid;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepositoryValid repository;

    private final String userEmail = "admin@gmail.com";

    @Test
    void 새로운_유저_생성() {
        GetNewUserDto getNewUserDto = 유저_데이터_생성();
        userService.createNewUser(getNewUserDto);
    }

    @Test
    void 이메일로_유저_찾기() {
        repository.findByUserEmail(userEmail);
    }

    GetNewUserDto 유저_데이터_생성() {
        return new GetNewUserDto(userEmail, "admin", "1234", "Student", "Default");
    }
}
