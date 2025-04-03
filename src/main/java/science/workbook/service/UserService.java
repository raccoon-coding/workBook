package science.workbook.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.config.jwt.JwtProvider;
import science.workbook.domain.EmailType;
import science.workbook.domain.Refresh;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.request.GetNewUserDto;
import science.workbook.dto.response.TokenDto;
import science.workbook.dto.toController.LoginDto;
import science.workbook.dto.toController.RefreshDto;
import science.workbook.dto.toService.ChangeUserPasswordDtoToService;
import science.workbook.dto.toService.CreateNewUserDtoToService;
import science.workbook.dto.toService.CreateTokenDtoToService;
import science.workbook.dto.toService.JoinUserDtoToService;
import science.workbook.dto.toService.SaveRefreshTokenDtoToService;
import science.workbook.exception.repository.NotFoundUserByEmail;
import science.workbook.exception.service.user.NotMatchPassword;
import science.workbook.exception.token.InvalidRefreshTokenException;
import science.workbook.repository.repositoryValid.UserRepositoryValid;

import java.util.List;

import static science.workbook.exception.constant.ApiErrorMessage.비밀번호_변경_에러;
import static science.workbook.exception.constant.ApiErrorMessage.비밀번호에러;
import static science.workbook.exception.constant.ApiErrorMessage.유저찾기실패;
import static science.workbook.exception.constant.ApiErrorMessage.재발급_토큰_에러;
import static science.workbook.util.UserUtil.generateRandomCode;

@Slf4j @Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryValid repository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public User createNewUser(GetNewUserDto controllerDto, EmailType emailType, Refresh refresh) {
        UserType userType = UserType.findUserType(controllerDto.getUserType());
        String encodePassword = encoder.encode(controllerDto.getUserPassword());
        CreateNewUserDtoToService dto = CreateNewUserDtoToService.createDefaultSsoType(controllerDto.getUserEmail(),
                controllerDto.getUserName(), encodePassword, userType);

        User newUser = new User(dto, emailType, refresh);
        repository.createNewUser(newUser);

        return newUser;
    }

    @Transactional
    public void deleteUser(User user) {
        repository.deleteUser(user);
    }

    @Transactional
    public void changeUserPassword(ChangeUserPasswordDtoToService dto) {
        User user = dto.user();
        if(!encoder.matches(user.getPassword(), dto.oldPassword())) throw new NotMatchPassword(비밀번호_변경_에러);

        String newPassword = encoder.encode(dto.newPassword());
        user.changePassword(newPassword);
        repository.changeUserPassword(user);
    }

    @Transactional
    public String findPasswordByUserEmail(String email, String username) {
        User user = findByUserEmail(email);

        if(user.validUser() && user.validUsername(username)){
            String newPassword = generateRandomCode(10);
            user.changePassword(encoder.encode(newPassword));
            return newPassword;
        }

        throw new NotFoundUserByEmail(유저찾기실패);
    }

    @Transactional
    public void deleteUsersByEmails(List<String> deleteEmails) {
        repository.deleteEmails(deleteEmails);
    }

    public boolean isEqualUserName(User user, String userName) {
        return user.getName().equals(userName);
    }

    public LoginDto loginUser(JoinUserDtoToService dto) {
        User user = findByUserEmail(dto.userEmail());

        if(!encoder.matches(dto.userPassword(), user.getPassword())) {
            throw new NotMatchPassword(비밀번호에러);
        }

        Refresh refresh = user.getRefresh();
        TokenDto tokenDto = jwtProvider.createToken(new CreateTokenDtoToService(user.getId(), refresh.getId()));
        SaveRefreshTokenDtoToService refreshTokenDto = new SaveRefreshTokenDtoToService(tokenDto.getRefreshToken(), tokenDto.getRefreshToken());
        return new LoginDto(tokenDto, refreshTokenDto);
    }

    public RefreshDto findUserByToken(HttpServletRequest request) {
        String jwt = jwtProvider.getToken(request);

        if(jwtProvider.validateRefreshToken(jwt)) {
            String userId = jwtProvider.getRefreshUserPk(jwt);
            User user = repository.findByUserId(userId);
            return new RefreshDto(user, jwt);
        }

        throw new InvalidRefreshTokenException(재발급_토큰_에러);
    }

    public Boolean validUserEmailAndName(String email, String name) {
        try {
            User user = findByUserEmail(email);

            if(user.getName().equals(name)) return Boolean.TRUE;
            return Boolean.FALSE;
        } catch (NotFoundUserByEmail e) {
            return Boolean.FALSE;
        }
    }

    public User findByUserEmail(String email) {
        return repository.findByUserEmail(email);
    }
}
