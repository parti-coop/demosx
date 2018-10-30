package seoul.democracy.proposal.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seoul.democracy.proposal.dto.ProposalDto;

public interface ProposalRepositoryCustom {

    Page<ProposalDto> findAll(Predicate predicate, Pageable pageable, Expression<ProposalDto> projection);

    ProposalDto findOne(Predicate predicate, Expression<ProposalDto> projection);
}
