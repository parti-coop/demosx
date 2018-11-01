package seoul.democracy.opinion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.opinion.domain.OpinionLike;

public interface OpinionLikeRepository extends JpaRepository<OpinionLike, Long>, QueryDslPredicateExecutor<OpinionLike> {
}
