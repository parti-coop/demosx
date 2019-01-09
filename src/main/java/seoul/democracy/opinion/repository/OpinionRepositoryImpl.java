package seoul.democracy.opinion.repository;

import com.mysema.query.SearchResults;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueType;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static seoul.democracy.issue.domain.QIssue.issue;
import static seoul.democracy.opinion.domain.QOpinion.opinion;
import static seoul.democracy.user.domain.QUser.user;
import static seoul.democracy.user.dto.UserDto.createdBy;
import static seoul.democracy.user.dto.UserDto.modifiedBy;

public class OpinionRepositoryImpl extends QueryDslRepositorySupport implements OpinionRepositoryCustom {

    final private static Expression<Long> constant = Expressions.constant(1L);

    public OpinionRepositoryImpl() {
        super(Opinion.class);
    }

    private JPQLQuery getQuery(Expression<OpinionDto> projection) {
        JPQLQuery query = from(opinion);
        if (projection == OpinionDto.projection) {
            query.innerJoin(opinion.createdBy, createdBy);
            query.innerJoin(opinion.modifiedBy, modifiedBy);
            query.innerJoin(opinion.issue, issue);
        } else if (projection == OpinionDto.projectionForIssueDetail) {
            query.innerJoin(opinion.createdBy, createdBy);
            query.innerJoin(opinion.issue, issue);
        } else if (projection == OpinionDto.projectionForMypage) {
            query.innerJoin(opinion.issue, issue);
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
    public OpinionDto findOne(Predicate predicate, Expression<OpinionDto> projection) {
        return getQuery(projection)
                   .where(predicate)
                   .uniqueResult(projection);
    }

    @Override
    public OpinionDto findOne(Predicate predicate, Expression<OpinionDto> projection, OrderSpecifier orderBy) {
        return getQuery(projection)
                   .where(predicate)
                   .orderBy(orderBy)
                   .singleResult(projection);
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

    @Override
    public List<Tuple> getStatsByDate(LocalDate date) {
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime endDateTime = LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0));

        return from(opinion)
                   .innerJoin(opinion.issue, issue)
                   .where(opinion.createdDate.goe(startDateTime).and(opinion.createdDate.lt(endDateTime)))
                   .groupBy(opinion.issue.type, opinion.issue.group)
                   .list(opinion.issue.type, opinion.issue.group, opinion.count());
    }

    @Override
    public List<Tuple> getNewOpinionProposal() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startDateTime = LocalDateTime.of(yesterday, LocalTime.of(0, 0));
        LocalDateTime endDateTime = LocalDateTime.of(yesterday.plusDays(1), LocalTime.of(0, 0));

        return from(opinion)
                   .innerJoin(opinion.issue, issue)
                   .innerJoin(opinion.issue.createdBy, user)
                   .where(opinion.issue.type.eq(IssueType.P)
                              .and(opinion.issue.status.eq(Issue.Status.OPEN))
                              .and(opinion.createdDate.goe(startDateTime))
                              .and(opinion.createdDate.lt(endDateTime)))
                   .groupBy(opinion.issue.id)
                   .list(opinion.issue.id, opinion.issue.createdBy.email, opinion.issue.createdBy.name);

    }
}
