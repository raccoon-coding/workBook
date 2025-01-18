package science.workbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.request.GetNewUserDto;
import science.workbook.dto.response.JoinCompleteDto;
import science.workbook.dto.toController.JoinUserInfo;
import science.workbook.dto.toService.ChangeUserPasswordDto;
import science.workbook.dto.toService.CreateNewUserDto;
import science.workbook.exception.repository.NotFoundUserByEmail;
import science.workbook.exception.service.user.NotMatchPassword;
import science.workbook.repository.repositoryValid.UserRepositoryValid;

import static science.workbook.exception.constant.ApiErrorMessage.비밀번호_변경_에러;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryValid repository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public JoinUserInfo createNewUser(GetNewUserDto controllerDto) {
        UserType userType = UserType.findUserType(controllerDto.getUserType());

        CreateNewUserDto dto = CreateNewUserDto.createDefaultSsoType(controllerDto.getUserEmail(),
                controllerDto.getUserName(), controllerDto.getUserPassword(), userType);

        // valid userData / unique key를 이름? 이메일??

        repository.createNewUser(dto);
        return new JoinUserInfo(dto);
    }

    @Transactional
    public void deleteUser(User user) {
        repository.deleteUser(user);
    }

    @Transactional
    public void changeUserPassword(ChangeUserPasswordDto dto) {
        User user = dto.user();
        if(!encoder.matches(user.getPassword(), dto.oldPassword())) {
            throw new NotMatchPassword(비밀번호_변경_에러);
        }
        String newPassword = encoder.encode(dto.newPassword());
        user.changePassword(newPassword);

        repository.changeUserPassword(user);
    }

    public User findByUserEmail(String email) {
        return repository.findByUserEmail(email);
    }

    public Boolean validUserEmailAndName(String email, String name) {
        try {
            User user = findByUserEmail(email);

            if(user.getName().equals(name)) {
                return Boolean.TRUE;
            }

            return Boolean.FALSE;
        } catch (NotFoundUserByEmail e) {
            return Boolean.FALSE;
        }
    }
}
