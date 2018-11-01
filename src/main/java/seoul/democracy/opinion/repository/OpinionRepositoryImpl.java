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
import seoul.democracy.opinion.dto.ProposalOpinionDto;

import static seoul.democracy.issue.domain.QIssue.issue;
import static seoul.democracy.opinion.domain.QProposalOpinion.proposalOpinion;
import static seoul.democracy.user.dto.UserDto.createdBy;
import static seoul.democracy.user.dto.UserDto.modifiedBy;

public class OpinionRepositoryImpl extends QueryDslRepositorySupport implements OpinionRepositoryCustom {

    final private static Expression<Long> constant = Expressions.constant(1L);

    public OpinionRepositoryImpl() {
        super(Opinion.class);
    }

    private JPQLQuery getQuery(Expression<ProposalOpinionDto> projection) {
        JPQLQuery query = from(proposalOpinion);
        if (projection == ProposalOpinionDto.projection) {
            query.innerJoin(proposalOpinion.createdBy, createdBy);
            query.innerJoin(proposalOpinion.modifiedBy, modifiedBy);
            query.innerJoin(proposalOpinion.issue, issue);
        }
        return query;
    }

    @Override
    public Page<ProposalOpinionDto> findAll(Predicate predicate, Pageable pageable, Expression<ProposalOpinionDto> projection) {
        SearchResults<ProposalOpinionDto> results = getQuerydsl()
                                                        .applyPagination(
                                                            pageable,
                                                            getQuery(projection)
                                                                .where(predicate))
                                                        .listResults(projection);
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public ProposalOpinionDto findOne(Predicate predicate, Expression<ProposalOpinionDto> projection) {
        return getQuery(projection)
                   .where(predicate)
                   .uniqueResult(projection);
    }

    @Override
    public void selectLike(Long opinionId) {
        update(proposalOpinion)
            .where(proposalOpinion.id.eq(opinionId))
            .set(proposalOpinion.likeCount, proposalOpinion.likeCount.add(constant))
            .execute();
    }

    @Override
    public void unselectLike(Long opinionId) {
        update(proposalOpinion)
            .where(proposalOpinion.id.eq(opinionId))
            .set(proposalOpinion.likeCount, proposalOpinion.likeCount.subtract(constant))
            .execute();
    }
}
