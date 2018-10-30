package seoul.democracy.proposal.predicate;

import com.mysema.query.types.Predicate;

import static seoul.democracy.proposal.domain.QProposal.proposal;

public class ProposalPredicate {

    public static Predicate equalId(Long id) {
        return proposal.id.eq(id);
    }
}
