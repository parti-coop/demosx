package seoul.democracy.debate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.issue.dto.IssueStatsDto;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        debate.status, debate.process,
        debate.thumbnail, debate.title, debate.content,
        debate.startDate, debate.endDate);

    public final static QBean<DebateDto> projectionForAdminList = Projections.fields(DebateDto.class,
        debate.id, debate.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        debate.opinionType,
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        debate.status, debate.process, debate.title);

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

    private Issue.Status status;
    private Debate.Process process;

    private String thumbnail;
    private String title;
    private String content;

    private LocalDate startDate;
    private LocalDate endDate;

    private List<Long> relations;
}
