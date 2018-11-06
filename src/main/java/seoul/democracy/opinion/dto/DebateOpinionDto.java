package seoul.democracy.opinion.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.user.dto.UserDto;

import static seoul.democracy.opinion.domain.QDebateOpinion.debateOpinion;

public class DebateOpinionDto extends OpinionDto {

    public final static QBean<DebateOpinionDto> projection = Projections.fields(DebateOpinionDto.class,
        debateOpinion.id, debateOpinion.createdDate, debateOpinion.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        debateOpinion.createdIp, debateOpinion.modifiedIp,
        IssueDto.projectionForBasic.as("issue"),
        debateOpinion.likeCount, debateOpinion.content, debateOpinion.status, debateOpinion.vote);

}
