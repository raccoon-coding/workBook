package science.workbook.repository.repositoryValid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import science.workbook.domain.SsoType;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.toService.CreateNewUserDtoToService;
import science.workbook.exception.repository.NotFoundUserByEmail;
import science.workbook.repository.repositoryMongo.UserRepository;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

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
        User newUser = new User(dto, any(), any());
        repository.createNewUser(newUser);

        유저_정보_삭제();
    }

    @Test
    void 이메일로_유저_찾기() {
        CreateNewUserDtoToService dto = 유저_정보();
        User newUser = new User(dto, any(), any());
        repository.createNewUser(newUser);

        User user = repository.findByUserEmail(userEmail);
        assertThat(user.getEmail()).isEqualTo(userEmail);

        유저_정보_삭제();
    }

    @Test
    void ID로_유저_찾기() {
        User newUser = 유저_생성();
        repository.createNewUser(newUser);
        User findUser = repository.findByUserEmail(userEmail);

        User user = repository.findByUserId(findUser.getId());
        assertThat(user.getEmail()).isEqualTo(findUser.getEmail());
        유저_정보_삭제();
    }

    @Test
    void 유저_삭제() {
        User newUser = 유저_생성();
        repository.createNewUser(newUser);

        repository.deleteUser(newUser);

        assertThatThrownBy(() -> repository.findByUserEmail(userEmail))
                .isInstanceOf(NotFoundUserByEmail.class);
    }

    @Test
    void 여러_이메일_삭제() {
        User user1 = new User(new CreateNewUserDtoToService("user1@gmail.com", "user1", "pass1", UserType.Student, SsoType.Default), any(), any());
        User user2 = new User(new CreateNewUserDtoToService("user2@gmail.com", "user2", "pass2", UserType.Student, SsoType.Default), any(), any());
        repository.createNewUser(user1);
        repository.createNewUser(user2);

        repository.deleteEmails(Arrays.asList("user1@gmail.com", "user2@gmail.com"));

        assertThatThrownBy(() -> repository.findByUserEmail("user1@gmail.com"))
                .isInstanceOf(NotFoundUserByEmail.class);
        assertThatThrownBy(() -> repository.findByUserEmail("user2@gmail.com"))
                .isInstanceOf(NotFoundUserByEmail.class);
    }

    @Test
    void 유저_비밀번호_변경() {
        User newUser = 유저_생성();
        repository.createNewUser(newUser);

        newUser.changePassword("newPassword");
        repository.changeUserPassword(newUser);

        User updatedUser = repository.findByUserEmail(userEmail);
        assertThat(updatedUser.getPassword()).isEqualTo("newPassword");

        유저_정보_삭제();
    }

    User 유저_생성() {
        return new User(유저_정보(), any(), any());
    }

    CreateNewUserDtoToService 유저_정보() {
        return new CreateNewUserDtoToService(userEmail, "admin", "1234", UserType.Student, SsoType.Default);
    }

    void 유저_정보_삭제() {
        repository.deleteUserByEmail(userEmail);
    }
}
