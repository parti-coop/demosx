package seoul.democracy.proposal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import seoul.democracy.proposal.domain.Proposal;

public interface ProposalRepository extends ProposalRepositoryCustom, JpaRepository<Proposal, Long>, QueryDslPredicateExecutor<Proposal> {
}
