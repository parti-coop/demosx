package seoul.democracy.debate.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;
import seoul.democracy.issue.domain.IssueGroup;

import static seoul.democracy.debate.domain.QDebate.debate;

public class DebatePredicate {

    public static Predicate equalId(Long id) {
        return debate.id.eq(id);
    }

    public static Predicate equalIdAndGroup(Long id, IssueGroup group) {
        return ExpressionUtils.and(debate.id.eq(id), debate.group.eq(group));
    }

    public static Predicate getPredicateForAdminList(IssueGroup group, String search, String category) {
        Predicate predicate = null;

        if (StringUtils.hasText(search))
            predicate = ExpressionUtils.or(debate.title.contains(search), debate.createdBy.name.contains(search));

        if (StringUtils.hasText(category))
            predicate = ExpressionUtils.and(predicate, debate.category.name.eq(category));

        return ExpressionUtils.and(predicate, debate.group.eq(group));
    }
}
