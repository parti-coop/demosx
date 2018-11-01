package seoul.democracy.opinion.repository;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.JPQLQuery;
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
import static seoul.democracy.user.domain.QUser.user;

public class OpinionRepositoryImpl extends QueryDslRepositorySupport implements OpinionRepositoryCustom {

    public OpinionRepositoryImpl() {
        super(Opinion.class);
    }

    private JPQLQuery getQuery(Expression<ProposalOpinionDto> projection) {
        JPQLQuery query = from(proposalOpinion);
        if (projection == ProposalOpinionDto.projection) {
            query.innerJoin(proposalOpinion.createdBy, user);
            query.innerJoin(proposalOpinion.modifiedBy, user);
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
}
