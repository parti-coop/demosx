package seoul.democracy.opinion.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

import static seoul.democracy.opinion.domain.QOpinionLike.opinionLike;

public class OpinionLikePredicate {
    public static Predicate equalUserId(Long userId) {
        return opinionLike.id.userId.eq(userId);
    }

    public static Predicate equalUserIdAndOpinionId(Long userId, Long issueId) {
        return ExpressionUtils.and(opinionLike.id.userId.eq(userId), opinionLike.id.opinionId.eq(issueId));
    }
}
