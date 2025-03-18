package science.workbook.repository.repositoryMongo;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import science.workbook.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
    @Query("{ 'createdAt' : { $gte: ?0 } }")
    List<User> findRecentUsers(LocalDateTime since, Sort sort);
    void deleteByEmailIn(List<String> emails);
}
