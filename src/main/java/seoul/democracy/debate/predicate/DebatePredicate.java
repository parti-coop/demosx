package seoul.democracy.debate.predicate;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueGroup;

import static seoul.democracy.debate.domain.QDebate.debate;
import static seoul.democracy.issue.domain.Issue.Status.OPEN;

public class DebatePredicate {

    public static Predicate equalId(Long id) {
        return debate.id.eq(id);
    }

    public static Predicate equalIdAndGroup(Long id, IssueGroup group) {
        return ExpressionUtils.and(debate.id.eq(id), debate.group.eq(group));
    }

    public static Predicate equalIdAndStatus(Long id, Issue.Status status) {
        return ExpressionUtils.and(debate.id.eq(id), debate.status.eq(status));
    }

    public static Predicate predicateForAdminList(IssueGroup group, String search, String category) {
        Predicate predicate = null;

        if (StringUtils.hasText(search))
            predicate = ExpressionUtils.or(debate.title.contains(search), debate.createdBy.name.contains(search));

        if (StringUtils.hasText(category))
            predicate = ExpressionUtils.and(predicate, debate.category.name.eq(category));

        return ExpressionUtils.and(predicate, debate.group.eq(group));
    }

    public static Predicate predicateForRelationSelect(String search) {
        Predicate predicate = ExpressionUtils.and(debate.group.eq(IssueGroup.USER), debate.status.eq(OPEN));

        if (StringUtils.isEmpty(search)) return predicate;

        return ExpressionUtils.and(predicate, debate.title.contains(search));
    }

    public static Predicate predicateForSiteList(IssueGroup group, Debate.Process process, String category, String search) {
        Predicate predicate = ExpressionUtils.and(debate.group.eq(group), debate.status.eq(OPEN));

        if (process != null)
            predicate = ExpressionUtils.and(predicate, debate.process.eq(process));

        if (!StringUtils.isEmpty(category))
            predicate = ExpressionUtils.and(predicate, debate.category.name.eq(category));

        if (StringUtils.isEmpty(search)) return predicate;

        return ExpressionUtils.and(predicate, debate.title.contains(search));
    }
}
