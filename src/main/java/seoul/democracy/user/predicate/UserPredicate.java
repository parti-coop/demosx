package seoul.democracy.user.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;
import seoul.democracy.user.domain.Role;

import static seoul.democracy.user.domain.QUser.user;

public class UserPredicate {

    public static Predicate equalId(Long id) {
        return user.id.eq(id);
    }

    public static Predicate equalEmail(String email) {
        return ExpressionUtils.and(
            user.email.eq(email),
            user.provider.eq("email")
        );
    }

    public static Predicate containsNameOrEmail(String search) {
        if (StringUtils.isEmpty(search)) return null;

        return ExpressionUtils.or(
            user.name.contains(search),
            ExpressionUtils.and(
                user.email.contains(search),
                user.provider.eq("email")
            )
        );
    }

    public static Predicate containsNameOrEmailAndRole(String search, Role role) {
        return ExpressionUtils.and(user.role.eq(role), containsNameOrEmail(search));
    }

    public static Predicate equalProviderAndId(String provider, String id) {
        return ExpressionUtils.and(
            user.email.eq(id),
            user.provider.eq(provider)
        );
    }
}
