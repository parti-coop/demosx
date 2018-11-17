package seoul.democracy.opinion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static seoul.democracy.opinion.domain.QOpinion.opinion;

@Data
@JsonInclude(NON_NULL)
public class OpinionDto {

    public final static QBean<OpinionDto> projection = Projections.fields(OpinionDto.class,
        opinion.id, opinion.createdDate, opinion.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        opinion.createdIp, opinion.modifiedIp,
        IssueDto.projectionForRelation.as("issue"),
        opinion.likeCount, opinion.content, opinion.status, opinion.vote);

    /**
     * 사이트 제안/토론 내 댓글
     */
    public final static QBean<OpinionDto> projectionForIssueDetail = Projections.fields(OpinionDto.class,
        opinion.id, opinion.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        opinion.likeCount, opinion.content, opinion.vote);

    protected Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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
