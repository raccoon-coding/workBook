package science.workbook.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {
    @Email(message = "이메일 형식이 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String userEmail;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String userPassword;
}
