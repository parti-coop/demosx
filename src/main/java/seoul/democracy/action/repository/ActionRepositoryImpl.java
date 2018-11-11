package seoul.democracy.action.repository;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.action.domain.Action;
import seoul.democracy.action.dto.ActionDto;
import seoul.democracy.issue.dto.IssueFileDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mysema.query.group.GroupBy.groupBy;
import static com.mysema.query.group.GroupBy.list;
import static seoul.democracy.action.domain.QAction.action;
import static seoul.democracy.issue.domain.QCategory.category;
import static seoul.democracy.issue.domain.QIssueFile.issueFile;
import static seoul.democracy.issue.domain.QIssueRelation.issueRelation;
import static seoul.democracy.issue.domain.QIssueStats.issueStats;
import static seoul.democracy.user.dto.UserDto.createdBy;
import static seoul.democracy.user.dto.UserDto.modifiedBy;

public class ActionRepositoryImpl extends QueryDslRepositorySupport implements ActionRepositoryCustom {

    public ActionRepositoryImpl() {
        super(Action.class);
    }

    private JPQLQuery getQuery(Expression<ActionDto> projection) {
        JPQLQuery query = from(action);
        if (projection == ActionDto.projection) {
            query.innerJoin(action.createdBy, createdBy);
            query.innerJoin(action.modifiedBy, modifiedBy);
            query.innerJoin(action.category, category);
            query.innerJoin(action.stats, issueStats);
        } else if (projection == ActionDto.projectionForAdminList || projection == ActionDto.projectionForAdminDetail) {
            query.innerJoin(action.createdBy, createdBy);
            query.innerJoin(action.category, category);
            query.innerJoin(action.stats, issueStats);
        }

        return query;
    }

    @Override
    public Page<ActionDto> findAll(Predicate predicate, Pageable pageable, Expression<ActionDto> projection, boolean withFiles, boolean withRelations) {
        SearchResults<ActionDto> results = getQuerydsl()
                                               .applyPagination(
                                                   pageable,
                                                   getQuery(projection)
                                                       .where(predicate))
                                               .listResults(projection);
        List<Long> actionIds = results.getResults().stream().map(ActionDto::getId).collect(Collectors.toList());
        if (withFiles && actionIds.size() != 0) {
            Map<Long, List<IssueFileDto>> filesMap = from(action)
                                                         .innerJoin(action.files, issueFile)
                                                         .where(action.id.in(actionIds))
                                                         .orderBy(issueFile.seq.asc())
                                                         .transform(groupBy(action.id).as(list(IssueFileDto.projection)));
            results.getResults().forEach(debateDto -> debateDto.setFiles(filesMap.get(debateDto.getId())));
        }

        if (withRelations && actionIds.size() != 0) {
            Map<Long, List<Long>> relationsMap = from(action)
                                                     .innerJoin(action.relations, issueRelation)
                                                     .where(action.id.in(actionIds))
                                                     .orderBy(issueRelation.seq.asc())
                                                     .transform(groupBy(action.id).as(list(issueRelation.issueId)));
            results.getResults().forEach(actionDto -> actionDto.setRelations(relationsMap.get(actionDto.getId())));
        }

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public ActionDto findOne(Predicate predicate, Expression<ActionDto> projection, boolean withFiles, boolean withRelations) {
        ActionDto actionDto = getQuery(projection)
                                  .where(predicate)
                                  .uniqueResult(projection);
        if (actionDto == null) return null;

        if (withFiles) {
            List<IssueFileDto> files = from(action)
                                           .innerJoin(action.files, issueFile)
                                           .where(predicate)
                                           .orderBy(issueFile.seq.asc())
                                           .list(IssueFileDto.projection);
            actionDto.setFiles(files);
        }

        if (withRelations) {
            List<Long> relations = from(action)
                                       .innerJoin(action.relations, issueRelation)
                                       .where(predicate)
                                       .orderBy(issueRelation.seq.asc())
                                       .list(issueRelation.issueId);
            actionDto.setRelations(relations);
        }

        return actionDto;
    }
}
