package seoul.democracy.opinion.repository;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.dto.ProposalOpinionDto;

import static seoul.democracy.issue.domain.QIssue.issue;
import static seoul.democracy.opinion.domain.QOpinion.opinion;
import static seoul.democracy.opinion.domain.QProposalOpinion.proposalOpinion;
import static seoul.democracy.user.dto.UserDto.createdBy;
import static seoul.democracy.user.dto.UserDto.modifiedBy;

public class OpinionRepositoryImpl extends QueryDslRepositorySupport implements OpinionRepositoryCustom {

    final private static Expression<Long> constant = Expressions.constant(1L);

    public OpinionRepositoryImpl() {
        super(Opinion.class);
    }

    private <T extends OpinionDto> JPQLQuery getQuery(Expression<T> projection) {
        JPQLQuery query;
        if (projection.getType() == ProposalOpinionDto.class)
            query = from(proposalOpinion);
        else
            query = from(opinion);

        if (projection == OpinionDto.projection) {
            query.innerJoin(opinion.createdBy, createdBy);
            query.innerJoin(opinion.modifiedBy, modifiedBy);
            query.innerJoin(opinion.issue, issue);
        } else if (projection == OpinionDto.projectionForIssueDetail) {
            query.innerJoin(opinion.createdBy, createdBy);
            query.innerJoin(opinion.issue, issue);
        } else if (projection == ProposalOpinionDto.projection) {
            query.innerJoin(proposalOpinion.createdBy, createdBy);
            query.innerJoin(proposalOpinion.modifiedBy, modifiedBy);
            query.innerJoin(proposalOpinion.issue, issue);
        }

        return query;
    }

    @Override
    public Page<OpinionDto> findAll(Predicate predicate, Pageable pageable, Expression<OpinionDto> projection) {
        SearchResults<OpinionDto> results = getQuerydsl()
                                                .applyPagination(
                                                    pageable,
                                                    getQuery(projection)
                                                        .where(predicate))
                                                .listResults(projection);
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public <T extends OpinionDto> T findOne(Predicate predicate, Expression<T> projection) {
        return getQuery(projection)
                   .where(predicate)
                   .uniqueResult(projection);
    }

    @Override
    public void increaseLike(Long opinionId) {
        update(opinion)
            .where(opinion.id.eq(opinionId))
            .set(opinion.likeCount, opinion.likeCount.add(constant))
            .execute();
    }

    @Override
    public void decreaseLike(Long opinionId) {
        update(opinion)
            .where(opinion.id.eq(opinionId))
            .set(opinion.likeCount, opinion.likeCount.subtract(constant))
            .execute();
    }
}
