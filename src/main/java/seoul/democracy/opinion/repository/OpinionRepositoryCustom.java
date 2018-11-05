package seoul.democracy.opinion.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seoul.democracy.opinion.dto.OpinionDto;

public interface OpinionRepositoryCustom {

    Page<OpinionDto> findAll(Predicate predicate, Pageable pageable, Expression<OpinionDto> projection);

    <T extends OpinionDto> T findOne(Predicate predicate, Expression<T> projection);


    /**
     * 제안의견 공감
     */
    void increaseLike(Long opinionId);

    /**
     * 제안의견 공감 해제
     */
    void decreaseLike(Long opinionId);
}
