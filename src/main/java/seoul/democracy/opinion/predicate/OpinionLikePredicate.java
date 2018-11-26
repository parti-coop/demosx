package seoul.democracy.opinion.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

import java.util.List;

import static seoul.democracy.opinion.domain.QOpinionLike.opinionLike;

public class OpinionLikePredicate {
    public static Predicate equalUserId(Long userId) {
        return opinionLike.id.userId.eq(userId);
    }

    public static Predicate equalUserIdAndOpinionId(Long userId, Long opinionId) {
        return ExpressionUtils.and(opinionLike.id.userId.eq(userId), opinionLike.id.opinionId.eq(opinionId));
    }

    public static Predicate equalUserIdAndOpinionIdIn(Long userId, List<Long> opinionIds) {
        return ExpressionUtils.and(opinionLike.id.userId.eq(userId), opinionLike.id.opinionId.in(opinionIds));
    }
}
