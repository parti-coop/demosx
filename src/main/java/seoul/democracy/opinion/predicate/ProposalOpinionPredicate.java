package seoul.democracy.opinion.predicate;

import com.mysema.query.types.Predicate;

import static seoul.democracy.opinion.domain.QProposalOpinion.proposalOpinion;

public class ProposalOpinionPredicate {
    public static Predicate equalId(Long id) {
        return proposalOpinion.id.eq(id);
    }
}
