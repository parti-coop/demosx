package seoul.democracy.opinion.repository;

import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seoul.democracy.opinion.dto.OpinionDto;

import java.time.LocalDate;
import java.util.List;

public interface OpinionRepositoryCustom {

    Page<OpinionDto> findAll(Predicate predicate, Pageable pageable, Expression<OpinionDto> projection);

    OpinionDto findOne(Predicate predicate, Expression<OpinionDto> projection);


    /**
     * 제안의견 공감
     */
    void increaseLike(Long opinionId);

    /**
     * 제안의견 공감 해제
     */
    void decreaseLike(Long opinionId);

    /**
     * 날짜 별 통계
     */
    List<Tuple> getStatsByDate(LocalDate date);

    /**
     * 새 의견이 있는 제안들
     */
    List<Tuple> getNewOpinionProposal();
}
