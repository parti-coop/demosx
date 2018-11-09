package seoul.democracy.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.issue.domain.Issue;

public interface IssueRepository extends IssueRepositoryCustom, JpaRepository<Issue, Long>, QueryDslPredicateExecutor<Issue> {
}
