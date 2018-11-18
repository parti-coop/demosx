package seoul.democracy.debate.repository;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.issue.dto.IssueFileDto;

import java.time.LocalDate;
import java.util.List;

import static seoul.democracy.debate.domain.QDebate.debate;
import static seoul.democracy.issue.domain.QCategory.category;
import static seoul.democracy.issue.domain.QIssueFile.issueFile;
import static seoul.democracy.issue.domain.QIssueRelation.issueRelation;
import static seoul.democracy.issue.domain.QIssueStats.issueStats;
import static seoul.democracy.user.dto.UserDto.createdBy;
import static seoul.democracy.user.dto.UserDto.modifiedBy;

public class DebateRepositoryImpl extends QueryDslRepositorySupport implements DebateRepositoryCustom {

    public DebateRepositoryImpl() {
        super(Debate.class);
    }

    private JPQLQuery getQuery(Expression<DebateDto> projection) {
        JPQLQuery query = from(debate);
        if (projection == DebateDto.projection) {
            query.innerJoin(debate.createdBy, createdBy);
            query.innerJoin(debate.modifiedBy, modifiedBy);
            query.innerJoin(debate.category, category);
            query.innerJoin(debate.stats, issueStats);
        } else if (projection == DebateDto.projectionForAdminList || projection == DebateDto.projectionForAdminDetail) {
            query.innerJoin(debate.createdBy, createdBy);
            query.innerJoin(debate.category, category);
            query.innerJoin(debate.stats, issueStats);
        } else if (projection == DebateDto.projectionForSiteList || projection == DebateDto.projectionForSiteDetail) {
            query.innerJoin(debate.category, category);
            query.innerJoin(debate.stats, issueStats);
        }
        return query;
    }

    @Override
    public Page<DebateDto> findAll(Predicate predicate, Pageable pageable, Expression<DebateDto> projection) {
        SearchResults<DebateDto> results = getQuerydsl()
                                               .applyPagination(
                                                   pageable,
                                                   getQuery(projection)
                                                       .where(predicate))
                                               .listResults(projection);
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public DebateDto findOne(Predicate predicate, Expression<DebateDto> projection, boolean withFiles, boolean withRelations) {
        DebateDto debateDto = getQuery(projection)
                                  .where(predicate)
                                  .uniqueResult(projection);

        if (debateDto != null && withFiles) {
            List<IssueFileDto> files = from(debate)
                                           .innerJoin(debate.files, issueFile)
                                           .where(predicate)
                                           .orderBy(issueFile.seq.asc())
                                           .list(IssueFileDto.projection);
            debateDto.setFiles(files);
        }

        if (debateDto != null && withRelations) {
            List<Long> relations = from(debate)
                                       .innerJoin(debate.relations, issueRelation)
                                       .where(predicate)
                                       .orderBy(issueRelation.seq.asc())
                                       .list(issueRelation.issueId);
            debateDto.setRelations(relations);
        }

        return debateDto;
    }

    @Override
    public void updateDebateProcess() {
        LocalDate now = LocalDate.now();
        // 전체를 진행 중 바꾼다.
        update(debate)
            .set(debate.process, Debate.Process.PROGRESS)
            .execute();

        // 종료일이 오늘 이전이면 종료로 바꾼다.
        update(debate)
            .where(debate.endDate.before(now))
            .set(debate.process, Debate.Process.COMPLETE)
            .execute();

        // 시작일이 오늘 이후면 진행예정으로 바꾼다.
        update(debate)
            .where(debate.startDate.after(now))
            .set(debate.process, Debate.Process.INIT)
            .execute();
    }
}
