package science.workbook.repository.repositoryValid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import science.workbook.domain.User;
import science.workbook.exception.repository.NotFoundUserByEmail;
import science.workbook.exception.repository.NotFoundUserById;
import science.workbook.repository.repositoryMongo.UserRepository;

import java.util.List;
import java.util.Optional;

import static science.workbook.exception.constant.ApiErrorMessage.유저찾기실패;

@Repository
@RequiredArgsConstructor
public class UserRepositoryValid {
    private final UserRepository repository;

    public void createNewUser(User newUser) {
        repository.save(newUser);
    }

    public void deleteUser(User user) {
        repository.delete(user);
    }

    public void deleteUserByEmail(String email) {
        repository.deleteByEmail(email);
    }

    public void deleteEmails(List<String> deleteEmails) {
        repository.deleteByEmailIn(deleteEmails);
    }

    public void changeUserPassword(User user) {
        repository.save(user);
    }

    public User findByUserEmail(String email) {
        Optional<User> optional = repository.findByEmail(email);
        if(optional.isPresent()) {
            return optional.get();
        }
        throw new NotFoundUserByEmail(유저찾기실패);
    }

    public User findByUserId(String Id) {
        Optional<User> optional = repository.findById(Id);
        if(optional.isPresent()) {
            return optional.get();
        }
        throw new NotFoundUserById(유저찾기실패);
    }
}
