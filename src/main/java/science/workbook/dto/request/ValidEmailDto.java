package science.workbook.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidEmailDto {
    @Email(message = "이메일 형식이 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String userEmail;

    @NotBlank(message = "코드을 입력해주세요.")
    @Size(min = 6 , message = "코드은 6자 이상 입력해야 합니다.")
    private String code;
}
