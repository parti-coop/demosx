package seoul.democracy.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.user.domain.User;

public interface UserRepository extends UserRepositoryCustom, JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {
}
