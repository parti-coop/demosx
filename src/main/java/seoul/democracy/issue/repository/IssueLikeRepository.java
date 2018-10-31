package seoul.democracy.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.issue.domain.IssueLike;

public interface IssueLikeRepository extends JpaRepository<IssueLike, Long>, QueryDslPredicateExecutor<IssueLike> {
}
