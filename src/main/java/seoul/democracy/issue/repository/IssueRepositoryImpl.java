package seoul.democracy.issue.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.dto.IssueDto;

import java.util.List;

import static seoul.democracy.issue.domain.QIssue.issue;

public class IssueRepositoryImpl extends QueryDslRepositorySupport implements IssueRepositoryCustom {

    public IssueRepositoryImpl() {
        super(Issue.class);
    }

    @Override
    public List<IssueDto> findAll(Predicate predicate, Expression<IssueDto> projection) {
        return from(issue)
                   .where(predicate)
                   .list(projection);
    }
}
