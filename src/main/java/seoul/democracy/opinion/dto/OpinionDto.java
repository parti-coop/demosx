package seoul.democracy.opinion.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDateTime;

import static seoul.democracy.opinion.domain.QOpinion.opinion;

@Data
public class OpinionDto {

    public final static QBean<OpinionDto> projection = Projections.fields(OpinionDto.class,
        opinion.id, opinion.createdDate, opinion.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        opinion.createdIp, opinion.modifiedIp,
        IssueDto.projectionForBasic.as("issue"),
        opinion.likeCount, opinion.content, opinion.status, opinion.vote);

    protected Long id;
    protected LocalDateTime createdDate;
    protected LocalDateTime modifiedDate;
    protected UserDto createdBy;
    protected UserDto modifiedBy;
    protected String createdIp;
    protected String modifiedIp;

    protected IssueDto issue;

    protected Long likeCount;
    protected String content;
    protected Opinion.Status status;
    protected Opinion.Vote vote;
}
