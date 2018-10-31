package seoul.democracy.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.issue.domain.IssueLike;
import seoul.democracy.issue.domain.IssueStats;

public interface IssueStatsRepository extends IssueStatsRepositoryCustom, JpaRepository<IssueStats, Long>, QueryDslPredicateExecutor<IssueLike> {
}
