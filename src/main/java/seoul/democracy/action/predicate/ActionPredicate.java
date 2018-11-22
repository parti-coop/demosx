package seoul.democracy.action.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;
import seoul.democracy.issue.domain.Issue;

import static seoul.democracy.action.domain.QAction.action;
import static seoul.democracy.issue.domain.Issue.Status.OPEN;

public class ActionPredicate {

    public static Predicate equalId(Long id) {
        return action.id.eq(id);
    }

    public static Predicate getPredicateForAdminList(String search, String category) {
        Predicate predicate = null;

        if (StringUtils.hasText(search))
            predicate = ExpressionUtils.or(action.title.contains(search), action.createdBy.name.contains(search));

        if (StringUtils.hasText(category))
            predicate = ExpressionUtils.and(predicate, action.category.name.eq(category));

        return predicate;
    }

    public static Predicate predicateForSiteList(String category) {

        if (StringUtils.isEmpty(category)) return action.status.eq(OPEN);

        return ExpressionUtils.and(action.status.eq(OPEN), action.category.name.eq(category));
    }

    public static Predicate equalIdAndStatus(Long id, Issue.Status status) {
        return ExpressionUtils.and(action.id.eq(id), action.status.eq(status));
    }
}
