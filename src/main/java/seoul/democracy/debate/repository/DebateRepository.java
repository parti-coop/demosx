package seoul.democracy.debate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.debate.domain.Debate;

public interface DebateRepository extends DebateRepositoryCustom, JpaRepository<Debate, Long>, QueryDslPredicateExecutor<Debate> {
}
