package seoul.democracy.user.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

import static seoul.democracy.user.domain.QUser.user;

public class UserPredicate {

    public static Predicate equalId(Long id) {
        return user.id.eq(id);
    }

    public static Predicate equalEmail(String email) {
        return user.email.eq(email);
    }

    public static Predicate containsNameAndEmail(String search) {
        return ExpressionUtils.or(user.name.contains(search), user.email.contains(search));
    }
}
