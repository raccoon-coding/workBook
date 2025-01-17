package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import science.workbook.domain.SsoType;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.request.GetNewUserDto;
import science.workbook.dto.toService.CreateNewUserDto;
import science.workbook.repository.repositoryValid.UserRepositoryValid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

        when(userService.findByUserEmail(userEmail)).thenReturn(stubbing());

        User user = userService.findByUserEmail(userEmail);
        assertThat(user.getEmail()).isEqualTo(userEmail);
    }

    @Test
    void 이메일로_유저_찾기() {
        repository.findByUserEmail(userEmail);
    }

    GetNewUserDto 유저_데이터_생성() {
        return new GetNewUserDto(userEmail, "admin", "1234", "Student", "Default");
    }

    User stubbing() {
        return new User(new CreateNewUserDto(userEmail, "admin", "1234", UserType.Student, SsoType.Default));
    }
}
