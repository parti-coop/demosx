package seoul.democracy.issue.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import seoul.democracy.issue.dto.IssueDto;

import java.util.List;

public interface IssueRepositoryCustom {

    List<IssueDto> findAll(Predicate predicate, Expression<IssueDto> projection);

}
