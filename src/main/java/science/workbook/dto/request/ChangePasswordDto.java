package science.workbook.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String oldPassword;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String newPassword;
}
