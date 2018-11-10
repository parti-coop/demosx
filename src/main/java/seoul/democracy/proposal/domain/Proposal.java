package seoul.democracy.proposal.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import seoul.democracy.common.converter.LocalDateTimeAttributeConverter;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueLike;
import seoul.democracy.issue.domain.IssueStats;
import seoul.democracy.opinion.domain.ProposalOpinion;
import seoul.democracy.opinion.dto.OpinionCreateDto;
import seoul.democracy.proposal.dto.ProposalCreateDto;
import seoul.democracy.proposal.dto.ProposalUpdateDto;
import seoul.democracy.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("P")
public class Proposal extends Issue {

    /**
     * 이슈 과정
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ISSUE_PROCESS")
    private Process process;

    /**
     * 관리자 댓글 일시
     */
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "ADMIN_COMMENT_DT")
    private LocalDateTime adminCommentDate;

    /**
     * 관리자 댓글
     */
    @Lob
    @Column(name = "ADMIN_COMMENT")
    private String adminComment;

    /**
     * 매니저 - 담당자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGER_ID")
    private User manager;
    @Column(name = "MANAGER_ID", insertable = false, updatable = false)
    private Long managerId;

    /**
     * 담당자 댓글 일시
     */
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "MANAGER_COMMENT_DT")
    private LocalDateTime managerCommentDate;

    /**
     * 담당자 댓글
     */
    @Lob
    @Column(name = "MANAGER_COMMENT")
    private String managerComment;

    public Proposal(String title, String content, String ip) {
        this.stats = IssueStats.create();
        this.status = Status.OPEN;
        this.process = Process.INIT;
        this.title = title;
        this.content = content;
        this.createdIp = this.modifiedIp = ip;

    }

    public static Proposal create(ProposalCreateDto createDto, String ip) {
        return new Proposal(createDto.getTitle(), createDto.getContent(), ip);
    }

    public Proposal update(ProposalUpdateDto updateDto, String ip) {
        if (!status.isOpen()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        this.modifiedIp = ip;
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        return this;
    }

    public Proposal delete(String ip) {
        if (!status.isOpen()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        this.modifiedIp = ip;
        this.status = Status.DELETE;
        return this;
    }

    public Proposal block(String ip) {
        if (!status.isOpen()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        this.status = Status.CLOSED;
        this.modifiedIp = ip;
        return this;
    }

    public Proposal open(String ip) {
        if (status.isDelete()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        this.status = Status.OPEN;
        this.modifiedIp = ip;
        return this;
    }

    public Proposal updateCategory(Category category, String ip) {
        this.category = category;
        this.modifiedIp = ip;
        return this;
    }

    public Proposal editAdminComment(String comment) {
        if (!status.isOpen()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        this.adminComment = comment;
        this.adminCommentDate = LocalDateTime.now();
        return this;
    }

    public Proposal assignManager(User manager) {
        if (!status.isOpen())
            throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        if (getStats().getLikeCount() < 50)
            throw new BadRequestException("likeCount", "error.likeCount", "공감수 50이상 제안만 담당자 지정이 가능합니다.");

        if (!manager.getRole().isManager())
            throw new BadRequestException("role", "error.role", "매니저만 담당자로 설정가능합니다.");

        this.manager = manager;
        this.process = Process.ASSIGNED;
        return this;
    }

    public Proposal editManagerComment(String comment) {
        this.managerComment = comment;
        this.managerCommentDate = LocalDateTime.now();
        this.process = Process.COMPLETE;
        return this;
    }

    @Override
    public ProposalOpinion createOpinion(OpinionCreateDto createDto, String ip) {
        return ProposalOpinion.create(this, createDto.getContent(), ip);
    }

    @Override
    public boolean isUpdatableOpinion() {
        return status.isOpen();
    }

    public IssueLike createLike(User user, String ip) {
        if (!status.isOpen()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        return IssueLike.create(user, this, ip);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Process {
        INIT(""),       // 초기상태
        ASSIGNED("답변대기"),   // 답변대기
        COMPLETE("부서답변");    // 부서답변

        private final String msg;
    }
}
