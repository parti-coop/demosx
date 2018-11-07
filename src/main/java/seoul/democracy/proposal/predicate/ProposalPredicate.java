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

    public static Predicate containsTitleOrCreatedByNameAndEqualCategory(String search, String category) {
        Predicate predicate = null;

        if (StringUtils.hasText(search))
            predicate = ExpressionUtils.or(proposal.title.contains(search), proposal.createdBy.name.contains(search));

        if (StringUtils.hasText(category))
            predicate = ExpressionUtils.and(predicate, proposal.category.name.eq(category));

        return predicate;
    }

    public static Predicate equalIdAndStatus(Long id, Issue.Status status) {
        return ExpressionUtils.and(proposal.id.eq(id), proposal.status.eq(status));
    }
}
