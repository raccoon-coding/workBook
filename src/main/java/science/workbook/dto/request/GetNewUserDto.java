package science.workbook.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetNewUserDto {
    @Email(message = "이메일 형식이 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String userEmail;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2 , message = "이름은 2자 이상 입력해야 합니다.")
    private String userName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String userPassword;

    @NotBlank(message = "유저 타입을 설정해주세요.")
    private String userType;

    @NotNull
    private String provider;
}
