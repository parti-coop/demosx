package seoul.democracy.issue.repository;

import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.dto.IssueDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Override
    public List<Tuple> getStatsByDate(LocalDate date) {
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime endDateTime = LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0));

        return from(issue)
                   .where(issue.createdDate.goe(startDateTime).and(issue.createdDate.lt(endDateTime)))
                   .groupBy(issue.type, issue.group)
                   .list(issue.type, issue.group, issue.count());
    }
}
