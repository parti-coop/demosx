package seoul.democracy.proposal.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.proposal.domain.Proposal;

import static seoul.democracy.issue.domain.Issue.Status.OPEN;
import static seoul.democracy.proposal.domain.QProposal.proposal;

public class ProposalPredicate {

    public static Predicate equalId(Long id) {
        return proposal.id.eq(id);
    }

    public static Predicate equalStatus(Issue.Status status) {
        return proposal.status.eq(status);
    }

    public static Predicate equalIdAndStatus(Long id, Issue.Status status) {
        return ExpressionUtils.and(proposal.id.eq(id), proposal.status.eq(status));
    }

    public static Predicate equalIdAndManagerId(Long id, Long managerId) {
        return ExpressionUtils.and(proposal.id.eq(id), proposal.managerId.eq(managerId));
    }

    public static Predicate predicateForRelationSelect(String search) {
        Predicate predicate = proposal.status.eq(OPEN);

        if (StringUtils.isEmpty(search)) return predicate;

        return ExpressionUtils.and(predicate, proposal.title.contains(search));
    }

    public static Predicate predicateForAdminList(String search, String category, Proposal.Process process) {
        Predicate predicate = null;

        if (StringUtils.hasText(search))
            predicate = ExpressionUtils.or(proposal.title.contains(search), proposal.createdBy.name.contains(search));

        if (StringUtils.hasText(category))
            predicate = ExpressionUtils.and(predicate, proposal.category.name.eq(category));

        if (process != null)
            predicate = ExpressionUtils.and(predicate, proposal.process.eq(process));

        return predicate;
    }

    public static Predicate predicateForManagerList(Long managerId, String search, String category, Proposal.Process process) {
        Predicate predicate = proposal.managerId.eq(managerId);

        return ExpressionUtils.and(predicate, predicateForAdminList(search, category, process));
    }

    public static Predicate predicateForSiteList(String search, String category) {
        Predicate predicate = proposal.status.eq(OPEN);

        if (StringUtils.hasText(search))
            predicate = ExpressionUtils.and(predicate, proposal.title.contains(search));

        if (StringUtils.hasText(category))
            predicate = ExpressionUtils.and(predicate, proposal.category.name.eq(category));

        return predicate;
    }

    public static Predicate predicateForMypageProposal(Long userId, String search) {
        Predicate predicate = ExpressionUtils.and(proposal.createdById.eq(userId), proposal.status.eq(OPEN));

        if (StringUtils.hasText(search))
            predicate = ExpressionUtils.and(predicate, proposal.title.contains(search));

        return predicate;
    }

    public static Predicate predicateForEdit(Long id, Long userId) {
        return ExpressionUtils.allOf(
            proposal.id.eq(id),
            proposal.createdById.eq(userId),
            proposal.status.eq(OPEN));
    }
}
