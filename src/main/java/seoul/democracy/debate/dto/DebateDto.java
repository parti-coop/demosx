package seoul.democracy.debate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueGroup;
import seoul.democracy.issue.domain.IssueType;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.issue.dto.IssueStatsDto;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static seoul.democracy.debate.domain.QDebate.debate;

@Data
public class DebateDto {

    public final static QBean<DebateDto> projection = Projections.fields(DebateDto.class,
        debate.id, debate.createdDate, debate.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        debate.createdIp, debate.modifiedIp, debate.opinionType,
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        debate.group, debate.status, debate.process,
        debate.thumbnail, debate.title, debate.excerpt, debate.content,
        debate.startDate, debate.endDate);

    /**
     * 관리자 토론 리스트에서 사용
     */
    public final static QBean<DebateDto> projectionForAdminList = Projections.fields(DebateDto.class,
        debate.id, debate.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        debate.opinionType,
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        debate.status, debate.process, debate.title);

    /**
     * 관리자 토론 상세에서 사용
     */
    public final static QBean<DebateDto> projectionForAdminDetail = Projections.fields(DebateDto.class,
        debate.id, debate.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        debate.opinionType,
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        debate.group, debate.status, debate.process,
        debate.thumbnail, debate.title, debate.excerpt, debate.content,
        debate.startDate, debate.endDate);

    /**
     * 토론 선택용으로 사용
     */
    public final static QBean<DebateDto> projectionForAdminSelect = Projections.fields(DebateDto.class,
        debate.id, debate.title);

    /**
     * 토론 리스트에서 사용
     */
    public final static QBean<DebateDto> projectionForSiteList = Projections.fields(DebateDto.class,
        debate.id, debate.opinionType,
        CategoryDto.projectionForFilter.as("category"),
        IssueStatsDto.projection.as("stats"),
        debate.group, debate.process, debate.thumbnail, debate.title, debate.excerpt, debate.startDate, debate.endDate);

    /**
     * 토론 상세에서 사용
     */
    public final static QBean<DebateDto> projectionForSiteDetail = Projections.fields(DebateDto.class,
        debate.id, debate.opinionType,
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        debate.group, debate.process, debate.thumbnail, debate.title, debate.content, debate.startDate, debate.endDate);

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UserDto createdBy;
    private UserDto modifiedBy;
    private String createdIp;
    private String modifiedIp;
    private OpinionType opinionType;

    private CategoryDto category;

    private IssueStatsDto stats;
    private List<IssueFileDto> files;

    private IssueGroup group;
    private Issue.Status status;
    private Debate.Process process;

    private String thumbnail;
    private String title;
    private String excerpt;
    private String content;

    private LocalDate startDate;
    private LocalDate endDate;

    private List<Long> relations;
    private Map<Long, IssueDto> issueMap;

    public List<IssueDto> viewProposals() {
        return relations.stream()
                   .map(relation -> issueMap.get(relation))
                   .filter(relation -> relation.getType() == IssueType.P)
                   .collect(Collectors.toList());
    }
}
