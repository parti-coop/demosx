package seoul.democracy.action.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;

import static seoul.democracy.action.domain.QAction.action;

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
}
