package seoul.democracy.history.repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.history.domain.IssueHistory;
import seoul.democracy.history.dto.IssueHistoryDto;

import java.util.List;

import static seoul.democracy.history.domain.QIssueHistory.issueHistory;
import static seoul.democracy.issue.domain.QIssue.issue;
import static seoul.democracy.user.dto.UserDto.createdBy;
import static seoul.democracy.user.dto.UserDto.modifiedBy;

public class IssueHistoryRepositoryImpl extends QueryDslRepositorySupport implements IssueHistoryRepositoryCustom {

    public IssueHistoryRepositoryImpl() {
        super(IssueHistory.class);
    }

    private JPQLQuery getQuery(Expression<IssueHistoryDto> projection) {
        JPQLQuery query = from(issueHistory);
        if (projection == IssueHistoryDto.projection) {
            query.innerJoin(issueHistory.createdBy, createdBy);
            query.innerJoin(issueHistory.modifiedBy, modifiedBy);
            query.innerJoin(issueHistory.issue, issue);
        } else if (projection == IssueHistoryDto.projectionForSite) {
            query.innerJoin(issueHistory.createdBy, createdBy);
        }
        return query;
    }

    @Override
    public List<IssueHistoryDto> findAll(Predicate predicate, Expression<IssueHistoryDto> projection) {
        return getQuery(projection)
                   .where(predicate)
                   .list(projection);
    }

    @Override
    public IssueHistoryDto findOne(Predicate predicate, Expression<IssueHistoryDto> projection) {
        return getQuery(projection)
                   .where(predicate)
                   .uniqueResult(projection);
    }
}
