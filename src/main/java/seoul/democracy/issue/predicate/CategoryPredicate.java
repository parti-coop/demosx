package seoul.democracy.issue.predicate;

import com.mysema.query.types.Predicate;

import static seoul.democracy.issue.domain.QCategory.category;

public class CategoryPredicate {

    public static Predicate equalId(Long id) {
        return category.id.eq(id);
    }

    public static Predicate equalName(String name) {
        return category.name.eq(name);
    }

    public static Predicate enabled() {
        return category.enabled.eq(true);
    }
}
