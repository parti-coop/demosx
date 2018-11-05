package seoul.democracy.history.predicate;

import com.mysema.query.types.Predicate;

import static seoul.democracy.history.domain.QIssueHistory.issueHistory;

public class IssueHistoryPredicate {

    public static Predicate equalId(Long id) {
        return issueHistory.id.eq(id);
    }

}
