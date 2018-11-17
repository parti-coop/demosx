package seoul.democracy.opinion.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import seoul.democracy.opinion.domain.Opinion;

import static seoul.democracy.issue.domain.Issue.Status.OPEN;
import static seoul.democracy.opinion.domain.QOpinion.opinion;

public class OpinionPredicate {

    public static Predicate equalId(Long id) {
        return opinion.id.eq(id);
    }

    public static Predicate equalIssueId(Long issueId) {
        return opinion.issue.id.eq(issueId);
    }

    public static Predicate equalIssueIdAndCreatedByIdAndStatus(Long issueId, Long userId, Opinion.Status status) {
        return ExpressionUtils.allOf(
            opinion.issue.id.eq(issueId),
            opinion.createdBy.id.eq(userId),
            opinion.status.eq(status));
    }

    public static Predicate predicateForOpinionList(Long issueId) {
        return ExpressionUtils.and(opinion.issue.id.eq(issueId), opinion.issue.status.eq(OPEN));
    }
}
