package seoul.democracy.proposal.repository;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalDto;

import static seoul.democracy.issue.domain.QCategory.category;
import static seoul.democracy.issue.domain.QIssueStats.issueStats;
import static seoul.democracy.proposal.domain.QProposal.proposal;
import static seoul.democracy.user.domain.QUser.user;

public class ProposalRepositoryImpl extends QueryDslRepositorySupport implements ProposalRepositoryCustom {

    public ProposalRepositoryImpl() {
        super(Proposal.class);
    }

    private JPQLQuery getQuery(Expression<ProposalDto> projection) {
        JPQLQuery query = from(proposal);
        if (projection == ProposalDto.projection) {
            query.innerJoin(proposal.createdBy, user);
            query.innerJoin(proposal.modifiedBy, user);
            query.innerJoin(proposal.category, category);
            query.innerJoin(proposal.stats, issueStats);
        }
        return query;
    }

    @Override
    public Page<ProposalDto> findAll(Predicate predicate, Pageable pageable, Expression<ProposalDto> projection) {
        SearchResults<ProposalDto> results = getQuerydsl()
                                                 .applyPagination(
                                                     pageable,
                                                     getQuery(projection)
                                                         .where(predicate))
                                                 .listResults(projection);
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public ProposalDto findOne(Predicate predicate, Expression<ProposalDto> projection) {
        return getQuery(projection)
                   .where(predicate)
                   .uniqueResult(projection);
    }
}
