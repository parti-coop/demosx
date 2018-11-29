package seoul.democracy.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoul.democracy.stats.domain.StatsIssue;

public interface StatsIssueRepository extends JpaRepository<StatsIssue, Long> {
}
