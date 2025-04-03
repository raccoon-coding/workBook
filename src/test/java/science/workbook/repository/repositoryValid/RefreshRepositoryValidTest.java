package science.workbook.repository.repositoryValid;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import science.workbook.domain.Refresh;
import science.workbook.domain.SsoType;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.toService.CreateNewUserDtoToService;
import science.workbook.exception.repository.NotFoundRefreshById;
import science.workbook.repository.repositoryMongo.RefreshRepository;
import science.workbook.repository.repositoryMongo.UserRepository;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@DataMongoTest
class RefreshRepositoryValidTest {
    private RefreshRepositoryValid repository;
    private UserRepositoryValid userRepository;

    private String email = "test@gmail.com";
    private String name = "test";
    private String password = "testPassword";

    @Autowired
    public RefreshRepositoryValidTest(RefreshRepository repository, UserRepository userRepository) {
        this.repository = new RefreshRepositoryValid(repository);
        this.userRepository = new UserRepositoryValid(userRepository);
    }

    @Test
    @DisplayName("Refresh Token 생성")
    void Refresh토큰_생성_확인() {
        CreateNewUserDtoToService dto = new CreateNewUserDtoToService(email, name, password, UserType.Student, SsoType.Default);
        User user = new User(dto, any() ,any());
        userRepository.createNewUser(user);

        Refresh refresh = new Refresh(name, email);
        repository.createToken(refresh);

        userRepository.deleteUser(user);
        repository.deleteToken(refresh);
    }

    @Test
    @DisplayName("Refresh Token Rotate 확인")
    void Refresh토큰_교환_확인() {
        CreateNewUserDtoToService dto = new CreateNewUserDtoToService(email, name, password, UserType.Student, SsoType.Default);
        User user = new User(dto, any(), any());
        userRepository.createNewUser(user);
        Refresh refresh = new Refresh(name, email);
        repository.createToken(refresh);

        String rotate = "rotateToken";
        refresh.rotate(rotate);
        repository.mergeToken(refresh);
        Refresh rotateToken = repository.findById(refresh.getId());
        assertThat(rotateToken.getToken()).isEqualTo(rotate);

        userRepository.deleteUser(user);
        repository.deleteToken(rotateToken);
    }

    @Test
    @DisplayName("Id로 Token 찾기")
    void Refresh토큰_Id_찾기_확인() {
        CreateNewUserDtoToService dto = new CreateNewUserDtoToService(email, name, password, UserType.Student, SsoType.Default);
        Refresh refresh = new Refresh(name, email);
        repository.createToken(refresh);
        User user = new User(dto, any(), refresh);
        userRepository.createNewUser(user);

        User findUser = userRepository.findByUserEmail(email);
        Refresh findRefresh = findUser.getRefresh();
        Refresh find = repository.findById(findRefresh.getId());
        assertThat(find.getToken()).isEqualTo(findRefresh.getToken());

        userRepository.deleteUser(findUser);
        repository.deleteToken(refresh);
    }

    @Test
    @DisplayName("Refresh Token 삭제 확인")
    void Refresh_토큰_삭제_확인() {
        Refresh refresh = new Refresh(name, email);
        repository.createToken(refresh);
        BigInteger id = refresh.getId();

        repository.deleteToken(refresh);
        assertThatThrownBy(() -> repository.findById(id))
                .isInstanceOf(NotFoundRefreshById.class);
    }
}
