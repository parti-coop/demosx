package seoul.democracy.opinion.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.opinion.domain.ProposalOpinion;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDateTime;

import static seoul.democracy.opinion.domain.QProposalOpinion.proposalOpinion;

@Data
public class ProposalOpinionDto {

    public final static QBean<ProposalOpinionDto> projection = Projections.fields(ProposalOpinionDto.class,
        proposalOpinion.id, proposalOpinion.createdDate, proposalOpinion.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        proposalOpinion.createdIp, proposalOpinion.modifiedIp,
        IssueDto.projectionForBasic.as("issue"),
        proposalOpinion.likeCount, proposalOpinion.content, proposalOpinion.status, proposalOpinion.vote);

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UserDto createdBy;
    private UserDto modifiedBy;
    private String createdIp;
    private String modifiedIp;

    private IssueDto issue;

    private Long likeCount;
    private String content;
    private ProposalOpinion.Status status;
    private ProposalOpinion.Vote vote;
}
