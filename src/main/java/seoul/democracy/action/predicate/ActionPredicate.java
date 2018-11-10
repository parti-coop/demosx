package seoul.democracy.action.predicate;

import com.mysema.query.types.Predicate;

import static seoul.democracy.action.domain.QAction.action;

public class ActionPredicate {

    public static Predicate equalId(Long id) {
        return action.id.eq(id);
    }
}
