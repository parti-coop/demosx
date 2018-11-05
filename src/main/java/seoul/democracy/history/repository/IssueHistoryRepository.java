package seoul.democracy.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.history.domain.IssueHistory;

public interface IssueHistoryRepository extends IssueHistoryRepositoryCustom, JpaRepository<IssueHistory, Long>, QueryDslPredicateExecutor<IssueHistory> {
}
