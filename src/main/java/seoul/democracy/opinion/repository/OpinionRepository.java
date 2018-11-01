package seoul.democracy.opinion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.opinion.domain.Opinion;

public interface OpinionRepository extends OpinionRepositoryCustom, JpaRepository<Opinion, Long>, QueryDslPredicateExecutor<Opinion> {
}
