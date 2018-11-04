package seoul.democracy.debate.predicate;

import com.mysema.query.types.Predicate;

import static seoul.democracy.debate.domain.QDebate.debate;

public class DebatePredicate {

    public static Predicate equalId(Long id) {
        return debate.id.eq(id);
    }
}
