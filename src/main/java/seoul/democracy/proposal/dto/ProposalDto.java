package seoul.democracy.proposal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueFile;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.IssueStatsDto;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static seoul.democracy.proposal.domain.QProposal.proposal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProposalDto {

    public final static QBean<ProposalDto> projection = Projections.fields(ProposalDto.class,
        proposal.id, proposal.createdDate, proposal.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        proposal.createdIp, proposal.modifiedIp, proposal.opinionType,
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        proposal.status, proposal.process,
        proposal.adminCommentDate, proposal.adminComment,
        UserDto.projectionForBasic.as("manager"),
        proposal.managerCommentDate, proposal.managerComment,
        proposal.title, proposal.content);

    /**
     * 관리자 제안 리스트에서 사용
     */
    public final static QBean<ProposalDto> projectionForAdminList = Projections.fields(ProposalDto.class,
        proposal.id, proposal.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        proposal.status, proposal.process,
        UserDto.projectionForBasic.as("manager"),
        proposal.title);

    /**
     * 관리자 제안 상세에서 사용
     */
    public final static QBean<ProposalDto> projectionForAdminDetail = Projections.fields(ProposalDto.class,
        proposal.id, proposal.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        proposal.status, proposal.process,
        proposal.adminComment,
        UserDto.projectionForBasic.as("manager"),
        proposal.managerComment,
        proposal.title, proposal.content);

    /**
     * 담당자 할당 후 사용
     */
    public final static QBean<ProposalDto> projectionForAssignManager = Projections.fields(ProposalDto.class,
        proposal.id, UserDto.projectionForBasic.as("manager"));

    /**
     * 제안 선택용으로 사용
     */
    public final static QBean<ProposalDto> projectionForAdminSelect = Projections.fields(ProposalDto.class,
        proposal.id, proposal.title);

    /**
     * 사이트 리스트에서 사용
     */
    public final static QBean<ProposalDto> projectionForSiteList = Projections.fields(ProposalDto.class,
        proposal.id, proposal.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        proposal.status, proposal.process,
        UserDto.projectionForBasic.as("manager"),
        proposal.title, proposal.excerpt);

    /**
     * 제안 상세에서 사용
     */
    public final static QBean<ProposalDto> projectionForSiteDetail = Projections.fields(ProposalDto.class,
        proposal.id, proposal.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        CategoryDto.projection.as("category"),
        IssueStatsDto.projection.as("stats"),
        proposal.status, proposal.process,
        proposal.adminCommentDate, proposal.adminComment,
        UserDto.projectionForBasic.as("manager"),
        proposal.managerCommentDate, proposal.managerComment,
        proposal.title, proposal.content);

    /**
     * 마이페이지에서 사용
     */
    public final static QBean<ProposalDto> projectionForMypageProposal = Projections.fields(ProposalDto.class,
        proposal.id, proposal.createdDate,
        IssueStatsDto.projection.as("stats"),
        proposal.status, proposal.process, proposal.title);

    /**
     * 나의 제안 수정 시 사용
     */
    public final static QBean<ProposalDto> projectionForSiteEdit = Projections.fields(ProposalDto.class,
        proposal.id, proposal.title, proposal.content);


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
    private List<IssueFile> files;

    private Issue.Status status;
    private Proposal.Process process;

    private LocalDateTime adminCommentDate;
    private String adminComment;

    private UserDto manager;
    private LocalDateTime managerCommentDate;
    private String managerComment;

    private String title;
    private String excerpt;
    private String content;

    // 제안에 대해 공감 표시 여부
    private Boolean liked;

    public String contentWithBr() {
        return content.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
    }
}
