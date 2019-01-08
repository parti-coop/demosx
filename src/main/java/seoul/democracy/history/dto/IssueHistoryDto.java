package seoul.democracy.history.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.history.domain.IssueHistory;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDateTime;

import static seoul.democracy.history.domain.QIssueHistory.issueHistory;

@Data
public class IssueHistoryDto {

    public final static QBean<IssueHistoryDto> projection = Projections.fields(IssueHistoryDto.class,
        issueHistory.id, issueHistory.createdDate, issueHistory.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        issueHistory.createdIp, issueHistory.modifiedIp,
        IssueDto.projectionForRelation.as("issue"),
        issueHistory.status, issueHistory.content);

    /**
     * 사이트에서
     */
    public final static QBean<IssueHistoryDto> projectionForSite = Projections.fields(IssueHistoryDto.class,
        issueHistory.id, issueHistory.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        issueHistory.status, issueHistory.content);

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UserDto createdBy;
    private UserDto modifiedBy;
    private String createdIp;
    private String modifiedIp;

    private IssueDto issue;

    private IssueHistory.Status status;
    private String content;

    public String contentWithBr() {
        return content.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
    }
}
