package seoul.democracy.debate.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seoul.democracy.debate.dto.DebateDto;

public interface DebateRepositoryCustom {

    Page<DebateDto> findAll(Predicate predicate, Pageable pageable, Expression<DebateDto> projection);

    DebateDto findOne(Predicate predicate, Expression<DebateDto> projection, boolean withFiles, boolean withRelations);

    void updateDebateProcess();
}
