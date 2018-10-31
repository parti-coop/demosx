package seoul.democracy.issue.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

import static seoul.democracy.issue.domain.QIssueLike.issueLike;

public class IssueLikePredicate {
    public static Predicate equalUserId(Long userId) {
        return issueLike.id.userId.eq(userId);
    }

    public static Predicate equalUserIdAndIssueId(Long userId, Long issueId) {
        return ExpressionUtils.and(issueLike.id.userId.eq(userId), issueLike.id.issueId.eq(issueId));
    }
}
