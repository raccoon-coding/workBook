package science.workbook.repository.repositoryValid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import science.workbook.domain.User;
import science.workbook.dto.toService.CreateNewUserDto;
import science.workbook.exception.repository.NotFoundUserByEmail;
import science.workbook.exception.repository.NotFoundUserById;
import science.workbook.repository.repositoryMongo.UserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryValid {
    private final UserRepository repository;

    public void createNewUser(CreateNewUserDto dto) {
        User user = new User(dto);
        repository.save(user);
    }

    public void deleteByUserEmail(String email) {
        repository.deleteByEmail(email);
    }

    public void deleteUser(User user) {
        repository.delete(user);
    }

    public void changeUserPassword(User user) {
        repository.save(user);
    }

    public User findByUserEmail(String email) {
        Optional<User> optional = repository.findByEmail(email);
        if(optional.isPresent()) {
            return optional.get();
        }
        throw new NotFoundUserByEmail("해당 이메일로 가입한 유저가 존재하지 않습니다.");
    }

    public User findByUserId(String Id) {
        Optional<User> optional = repository.findById(Id);
        if(optional.isPresent()) {
            return optional.get();
        }
        throw new NotFoundUserById("해당 토큰은 저희 서버에서 발급한 토큰이 아닙니다.");
    }
}
