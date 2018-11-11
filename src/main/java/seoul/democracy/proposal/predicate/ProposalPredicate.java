package seoul.democracy.proposal.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;
import seoul.democracy.issue.domain.Issue;

import static seoul.democracy.proposal.domain.QProposal.proposal;

public class ProposalPredicate {

    public static Predicate equalId(Long id) {
        return proposal.id.eq(id);
    }

    public static Predicate getPredicateForAdminList(String search, String category) {
        Predicate predicate = null;

        if (StringUtils.hasText(search))
            predicate = ExpressionUtils.or(proposal.title.contains(search), proposal.createdBy.name.contains(search));

        if (StringUtils.hasText(category))
            predicate = ExpressionUtils.and(predicate, proposal.category.name.eq(category));

        return predicate;
    }

    public static Predicate getPredicateForRelationSelect(String search) {
        Predicate predicate = proposal.status.eq(Issue.Status.OPEN);

        if (StringUtils.isEmpty(search)) return predicate;

        return ExpressionUtils.and(predicate, proposal.title.contains(search));
    }
}
