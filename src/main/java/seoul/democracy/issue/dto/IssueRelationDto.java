package seoul.democracy.issue.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;

import static seoul.democracy.issue.domain.QIssueRelation.issueRelation;

@Data
public class IssueRelationDto {

    public final static QBean<IssueRelationDto> projection = Projections.fields(IssueRelationDto.class,
        issueRelation.issueId);

}
