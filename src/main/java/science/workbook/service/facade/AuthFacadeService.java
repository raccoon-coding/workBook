package science.workbook.service.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.domain.EmailType;
import science.workbook.domain.Refresh;
import science.workbook.domain.User;
import science.workbook.dto.request.GetNewUserDto;
import science.workbook.dto.response.JoinCompleteDto;
import science.workbook.service.FileService;
import science.workbook.service.MailService;
import science.workbook.service.RefreshService;
import science.workbook.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthFacadeService {
    private final UserService userService;
    private final MailService mailService;
    private final FileService fileService;
    private final RefreshService refreshService;

    @Transactional
    public JoinCompleteDto register(GetNewUserDto dto) {
        EmailType emailType = mailService.createVerificationCode(dto.getUserEmail());
        String content = mailService.setContextValidEmail(dto.getUserName(), emailType.getCode());
        mailService.sendEmailNotice(emailType.getEmail(), "회원 가입 인증", content);

        Refresh refresh = refreshService.createRefresh(dto.getUserName(), dto.getUserEmail());

        User newUser = userService.createNewUser(dto, emailType, refresh);
        return new JoinCompleteDto(newUser);
    }

    @Transactional
    public void deleteUser(User deleteUser) {
        String deleteUserEmail = deleteUser.getEmail();
        String deleteUserName = deleteUser.getName();

        mailService.deleteEmailType(deleteUserEmail);
        refreshService.deleteRefresh(deleteUserEmail);
        fileService.deleteUserDirectory(deleteUserName);
        userService.deleteUser(deleteUser);
    }
}
