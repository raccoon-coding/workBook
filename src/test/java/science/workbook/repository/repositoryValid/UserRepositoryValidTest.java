package science.workbook.repository.repositoryValid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import science.workbook.domain.SsoType;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.toService.CreateNewUserDtoToService;
import science.workbook.repository.repositoryMongo.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class UserRepositoryValidTest {
    private final UserRepositoryValid repository;

    private final String userEmail = "admin@gmail.com";

    @Autowired
    public UserRepositoryValidTest(UserRepository repository) {
        this.repository = new UserRepositoryValid(repository);
    }

    @Test
    void 새로운_유저_생성() {
        CreateNewUserDtoToService dto = 유저_정보();
        repository.createNewUser(dto);

        유저_정보_삭제();
    }

    @Test
    void 이메일로_유저_찾기() {
        CreateNewUserDtoToService dto = 유저_정보();
        repository.createNewUser(dto);

        User user = repository.findByUserEmail(userEmail);
        assertThat(user.getEmail()).isEqualTo(userEmail);

        유저_정보_삭제();
    }

    CreateNewUserDtoToService 유저_정보() {
        return new CreateNewUserDtoToService(userEmail, "admin", "1234", UserType.Student, SsoType.Default);
    }

    void 유저_정보_삭제() {
        repository.deleteByUserEmail(userEmail);
    }
}
