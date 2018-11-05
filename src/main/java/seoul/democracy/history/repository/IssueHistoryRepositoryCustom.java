package seoul.democracy.history.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import seoul.democracy.history.dto.IssueHistoryDto;

import java.util.List;

public interface IssueHistoryRepositoryCustom {

    List<IssueHistoryDto> findAll(Predicate predicate, Expression<IssueHistoryDto> projection);

    IssueHistoryDto findOne(Predicate predicate, Expression<IssueHistoryDto> projection);
}
