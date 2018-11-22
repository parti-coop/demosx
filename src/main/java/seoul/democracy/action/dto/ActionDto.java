package seoul.democracy.action.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueType;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.issue.dto.IssueStatsDto;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static seoul.democracy.action.domain.QAction.action;


@Data
public class ActionDto {

    public final static QBean<ActionDto> projection = Projections.fields(ActionDto.class,
        action.id, action.createdDate, action.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        action.createdIp, action.modifiedIp,
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        action.status, action.thumbnail, action.title, action.content);

    /**
     * 관리자 실행 리스트에서 사용
     */
    public final static QBean<ActionDto> projectionForAdminList = Projections.fields(ActionDto.class,
        action.id, action.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        action.status, action.title);

    /**
     * 관리자 실행 상세에서 사용
     */
    public final static QBean<ActionDto> projectionForAdminDetail = Projections.fields(ActionDto.class,
        action.id, action.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        action.status, action.thumbnail, action.title, action.content);

    /**
     * 사이트 실행 리스트에서 사용
     */
    public final static QBean<ActionDto> projectionForSiteList = Projections.fields(ActionDto.class,
        action.id, action.thumbnail, action.title);

    /**
     * 관리자 실행 상세에서 사용
     */
    public final static QBean<ActionDto> projectionForSiteDetail = Projections.fields(ActionDto.class,
        action.id, action.title, action.content);

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UserDto createdBy;
    private UserDto modifiedBy;
    private String createdIp;
    private String modifiedIp;

    private CategoryDto category;
    private IssueStatsDto stats;

    private Issue.Status status;

    private String thumbnail;
    private String title;
    private String content;

    private List<IssueFileDto> files;

    private List<Long> relations;
    private Map<Long, IssueDto> issueMap;

    public List<IssueDto> viewProposals() {
        return relations.stream()
                   .map(relation -> issueMap.get(relation))
                   .filter(relation -> relation.getType() == IssueType.P)
                   .collect(Collectors.toList());
    }

    public List<IssueDto> viewDebates() {
        return relations.stream()
                   .map(relation -> issueMap.get(relation))
                   .filter(relation -> relation.getType() == IssueType.D)
                   .collect(Collectors.toList());
    }
}
