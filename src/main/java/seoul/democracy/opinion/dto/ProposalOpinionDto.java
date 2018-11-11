package seoul.democracy.opinion.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.user.dto.UserDto;

import static seoul.democracy.opinion.domain.QProposalOpinion.proposalOpinion;

public class ProposalOpinionDto extends OpinionDto {

    public final static QBean<ProposalOpinionDto> projection = Projections.fields(ProposalOpinionDto.class,
        proposalOpinion.id, proposalOpinion.createdDate, proposalOpinion.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        proposalOpinion.createdIp, proposalOpinion.modifiedIp,
        IssueDto.projectionForRelation.as("issue"),
        proposalOpinion.likeCount, proposalOpinion.content, proposalOpinion.status, proposalOpinion.vote);

}
