package seoul.democracy.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoul.democracy.stats.domain.StatsOpinion;

public interface StatsOpinionRepository extends JpaRepository<StatsOpinion, Long> {
}
