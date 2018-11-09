package seoul.democracy.issue.predicate;

import com.mysema.query.types.Predicate;

import java.util.List;

import static seoul.democracy.issue.domain.QIssue.issue;

public class IssuePredicate {

    public static Predicate equalIdIn(List<Long> relations) {
        return issue.id.in(relations);
    }
}
