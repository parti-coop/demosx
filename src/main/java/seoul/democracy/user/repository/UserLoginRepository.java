package seoul.democracy.user.repository;

import org.springframework.data.repository.CrudRepository;
import seoul.democracy.user.domain.UserLogin;

public interface UserLoginRepository extends CrudRepository<UserLogin, Long> {
}
