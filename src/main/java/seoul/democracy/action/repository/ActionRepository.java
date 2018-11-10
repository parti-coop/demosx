package seoul.democracy.action.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.action.domain.Action;

public interface ActionRepository extends ActionRepositoryCustom, JpaRepository<Action, Long>, QueryDslPredicateExecutor<Action> {
}
