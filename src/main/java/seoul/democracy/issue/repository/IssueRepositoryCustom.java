package seoul.democracy.issue.repository;

import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import seoul.democracy.issue.dto.IssueDto;

import java.time.LocalDate;
import java.util.List;

public interface IssueRepositoryCustom {

    List<IssueDto> findAll(Predicate predicate, Expression<IssueDto> projection);

    /**
     * 날짜 별 통계
     */
    List<Tuple> getStatsByDate(LocalDate date);
}
