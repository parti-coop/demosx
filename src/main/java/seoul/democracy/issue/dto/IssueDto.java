package seoul.democracy.issue.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueFile;
import seoul.democracy.issue.domain.IssueGroup;
import seoul.democracy.issue.domain.IssueType;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static seoul.democracy.issue.domain.QIssue.issue;

@Data
public class IssueDto {

    public final static QBean<IssueDto> projectionForRelation = Projections.fields(IssueDto.class,
        issue.id, issue.type, issue.title);

    public final static QBean<IssueDto> projectionForOpinion = Projections.fields(IssueDto.class,
        issue.id, issue.type, issue.title, issue.group, issue.status);

    private Long id;
    private IssueType type;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UserDto createdBy;
    private UserDto modifiedBy;
    private String createdIp;
    private String modifiedIp;
    private OpinionType opinionType;

    private IssueGroup group;
    private Issue.Status status;

    private CategoryDto category;

    private IssueStatsDto stats;
    private List<IssueFile> files;

    private String title;
    private String content;
}
