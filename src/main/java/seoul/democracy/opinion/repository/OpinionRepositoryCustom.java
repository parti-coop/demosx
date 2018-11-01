package seoul.democracy.opinion.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seoul.democracy.opinion.dto.ProposalOpinionDto;

public interface OpinionRepositoryCustom {

    Page<ProposalOpinionDto> findAll(Predicate predicate, Pageable pageable, Expression<ProposalOpinionDto> projection);

    ProposalOpinionDto findOne(Predicate predicate, Expression<ProposalOpinionDto> projection);


    /**
     * 제안의견 공감
     */
    void selectLike(Long opinionId);

    /**
     * 제안의견 공감 해제
     */
    void unselectLike(Long opinionId);
}
