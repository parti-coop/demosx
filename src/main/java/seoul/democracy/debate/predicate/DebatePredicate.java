package seoul.democracy.debate.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;

import static seoul.democracy.debate.domain.QDebate.debate;

public class DebatePredicate {

    public static Predicate equalId(Long id) {
        return debate.id.eq(id);
    }

    public static Predicate containsTitleOrCreatedByNameAndEqualCategory(String search, String category) {
        Predicate predicate = null;

        if (StringUtils.hasText(search))
            predicate = ExpressionUtils.or(debate.title.contains(search), debate.createdBy.name.contains(search));

        if (StringUtils.hasText(category))
            predicate = ExpressionUtils.and(predicate, debate.category.name.eq(category));

        return predicate;
    }
}
