package seoul.democracy.proposal.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.democracy.common.converter.LocalDateTimeAttributeConverter;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.domain.IssueStats;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.opinion.domain.ProposalOpinion;
import seoul.democracy.proposal.dto.ProposalCreateDto;
import seoul.democracy.proposal.dto.ProposalUpdateDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("P")
public class Proposal extends Issue {

    /**
     * 이슈 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ISSUE_STATUS")
    private Status status;

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

    public Proposal(Category category, String title, String content, String ip) {
        this.stats = IssueStats.create();
        this.status = Status.OPEN;
        this.opinionType = OpinionType.PROPOSAL;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdIp = ip;
        this.modifiedIp = ip;

    }

    public static Proposal create(ProposalCreateDto createDto, Category category, String ip) {
        return new Proposal(category, createDto.getTitle(), createDto.getContent(), ip);
    }

    public Proposal update(ProposalUpdateDto updateDto, String ip) {
        this.modifiedIp = ip;
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();

        return this;
    }

    public Proposal delete(String ip) {
        this.modifiedIp = ip;
        this.status = Status.DELETE;

        return this;
    }

    public ProposalOpinion createOpinion(String content, String ip) {
        return ProposalOpinion.create(this, content, ip);
    }

    public Proposal editAdminComment(String comment) {
        this.adminComment = comment;
        this.adminCommentDate = LocalDateTime.now();
        return this;
    }

    public enum Status {
        OPEN,       // 공개
        DELETE,     // 삭제
        BLOCK;        // 관리자삭제

        public boolean isDelete() {
            return this == DELETE;
        }

        public boolean isBlock() {
            return this == BLOCK;
        }
    }
}