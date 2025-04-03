package science.workbook.repository.repositoryValid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import science.workbook.domain.Refresh;
import science.workbook.exception.repository.NotFoundRefreshById;
import science.workbook.repository.repositoryMongo.RefreshRepository;

import java.math.BigInteger;
import java.util.Optional;

import static science.workbook.exception.constant.ApiErrorMessage.재발급_토큰_에러;

@Repository
@RequiredArgsConstructor
public class RefreshRepositoryValid {
    private final RefreshRepository repository;

    public Refresh findById(BigInteger id) {
        Optional<Refresh> optional = repository.findById(id);
        if(optional.isPresent()) {
            return optional.get();
        }
        throw new NotFoundRefreshById(재발급_토큰_에러);
    }

    public void createToken(Refresh refresh) {
        repository.save(refresh);
    }

    public void mergeToken(Refresh refresh) {
        repository.save(refresh);
    }

    public void deleteToken(Refresh refresh) {
        repository.delete(refresh);
    }
}
