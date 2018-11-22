package seoul.democracy.action.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seoul.democracy.action.dto.ActionDto;

public interface ActionRepositoryCustom {

    Page<ActionDto> findAll(Predicate predicate, Pageable pageable, Expression<ActionDto> projection);

    ActionDto findOne(Predicate predicate, Expression<ActionDto> projection, boolean withFiles, boolean withRelations);
}
