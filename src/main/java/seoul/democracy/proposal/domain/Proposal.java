package seoul.democracy.proposal.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;
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

    public Proposal(String title, String content) {
        this.stats = IssueStats.create();
        this.status = Status.OPEN;
        this.process = Process.INIT;
        this.title = title;
        this.content = content;
        updateExcerpt(this.content);
    }

    private void updateExcerpt(String content) {
        if (StringUtils.hasText(content)) {
            String unescapeString = HtmlUtils.htmlUnescape(content).replaceAll("<.*?>", "").trim();
            this.excerpt = (unescapeString.length() > 100) ? unescapeString.substring(0, 100) : unescapeString;
        }
    }

    public static Proposal create(ProposalCreateDto createDto) {
        return new Proposal(createDto.getTitle(), createDto.getContent());
    }

    public Proposal update(ProposalUpdateDto updateDto) {
        if (!status.isOpen()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        updateExcerpt(this.content);

        return this;
    }

    public Proposal delete() {
        if (!status.isOpen()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        this.status = Status.DELETE;
        return this;
    }

    public Proposal block() {
        if (!status.isOpen()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        this.status = Status.CLOSED;
        return this;
    }

    public Proposal open() {
        if (status.isDelete()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        this.status = Status.OPEN;
        return this;
    }

    public Proposal updateCategory(Category category) {
        this.category = category;
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

        if (process.isInit())
            throw new BadRequestException("likeCount", "error.likeCount", "공감수 50이상 제안만 담당자 지정이 가능합니다.");

        if (process.isComplete())
            throw new BadRequestException("process", "error.process", "담당자 답변이 완료된 경우 담당자를 변경할 수 없습니다.");

        if (!manager.isManager())
            throw new BadRequestException("role", "error.role", "담당자로 지정되어 있지 않습니다.");

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
    public ProposalOpinion createOpinion(OpinionCreateDto createDto) {
        return ProposalOpinion.create(this, createDto.getContent());
    }

    @Override
    public boolean isUpdatableOpinion() {
        return status.isOpen();
    }

    public IssueLike createLike(User user) {
        if (!status.isOpen()) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        if (process.isInit() && stats.getLikeCount() >= 50)
            process = Process.NEED_ASSIGN;

        return IssueLike.create(user, this);
    }

    public void deleteLike() {
        if (process.isNeedAssign() && stats.getLikeCount() < 50)
            process = Process.INIT;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Process {
        INIT("일반"),       // 일반
        NEED_ASSIGN("담당지정대기"), // 담당지정대기
        ASSIGNED("답변대기"),       // 답변대기
        COMPLETE("부서답변");       // 부서답변

        private final String msg;

        public boolean isInit() {
            return this == INIT;
        }

        public boolean isNeedAssign() {
            return this == NEED_ASSIGN;
        }

        public boolean isAssigned() {
            return this == ASSIGNED;
        }

        public boolean isComplete() {
            return this == COMPLETE;
        }
    }
}
