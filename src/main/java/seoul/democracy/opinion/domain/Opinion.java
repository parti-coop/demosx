package seoul.democracy.opinion.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoul.democracy.common.annotation.CreatedIp;
import seoul.democracy.common.annotation.ModifiedIp;
import seoul.democracy.common.converter.LocalDateTimeAttributeConverter;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.common.listener.AuditingIpListener;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.opinion.dto.OpinionUpdateDto;
import seoul.democracy.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 의견
 */
@Getter
@NoArgsConstructor
@Entity(name = "TB_OPINION")
@EntityListeners({AuditingEntityListener.class, AuditingIpListener.class})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "OPINION_DTYPE", columnDefinition = "char(1)")
public abstract class Opinion {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "OPINION_ID")
    private Long id;

    /**
     * 의견 종류
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "OPINION_DTYPE", columnDefinition = "char(1)", insertable = false, updatable = false)
    private Type type;

    /**
     * 등록 일시
     */
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "REG_DT", insertable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * 수정 일시
     */
    @LastModifiedDate
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "CHG_DT", insertable = false)
    private LocalDateTime modifiedDate;

    /**
     * 등록 ID
     */
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REG_ID", updatable = false, nullable = false)
    private User createdBy;
    @Column(name = "REG_ID", insertable = false, updatable = false)
    private Long createdById;

    /**
     * 수정 ID
     */
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CHG_ID", nullable = false)
    private User modifiedBy;

    /**
     * 등록 아이피
     */
    @CreatedIp
    @Column(name = "REG_IP", updatable = false)
    private String createdIp;

    /**
     * 수정 아이피
     */
    @ModifiedIp
    @Column(name = "CHG_IP")
    private String modifiedIp;

    /**
     * 이슈
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ISSUE_ID", updatable = false, nullable = false)
    private Issue issue;

    /**
     * 공감수
     */
    @Column(name = "LIKE_CNT", insertable = false, updatable = false)
    private Long likeCount;

    /**
     * 의견 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "OPINION_STATUS")
    private Status status;

    /**
     * 의견 투표
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "OPINION_VOTE", updatable = false)
    protected Vote vote;

    /**
     * 의견 내용
     */
    @Column(name = "OPINION_CONTENT")
    protected String content;

    Opinion(Issue issue, String content) {
        this.issue = issue;
        this.content = content;

        this.status = Status.OPEN;
    }

    public Opinion update(OpinionUpdateDto updateDto) {
        if (!issue.isUpdatableOpinion())
            throw new BadRequestException("process", "error.process", "해당 의견은 수정할 수 없습니다.");

        if (!status.isOpen()) throw new NotFoundException("해당 의견을 찾을 수 없습니다.");

        this.content = updateDto.getContent();
        return this;
    }

    public Opinion delete() {
        if (!status.isOpen()) throw new NotFoundException("해당 의견을 찾을 수 없습니다.");

        this.status = Status.DELETE;
        return this;
    }

    public Opinion block() {
        if (!status.isOpen()) throw new NotFoundException("해당 의견을 찾을 수 없습니다.");

        this.status = Status.BLOCK;
        return this;
    }

    public Opinion open() {
        if (status.isOpen()) throw new BadRequestException("status", "error.status", "이미 공개된 의견입니다.");
        if (status.isDelete()) throw new NotFoundException("해당 의견을 찾을 수 없습니다.");

        this.status = Status.OPEN;
        return this;
    }

    public OpinionLike createLike(User user) {
        if (!status.isOpen()) throw new NotFoundException("해당 의견을 찾을 수 없습니다.");

        return OpinionLike.create(user, this);
    }

    public enum Status {
        OPEN,
        DELETE,
        BLOCK;

        public boolean isOpen() {
            return this == OPEN;
        }

        public boolean isDelete() {
            return this == DELETE;
        }

        public boolean isBlock() {
            return this == BLOCK;
        }
    }

    public enum Type {
        P,
        D;

        public boolean isProposal() {
            return this == P;
        }

        public boolean isDebate() {
            return this == D;
        }
    }

    public enum Vote {
        YES,
        NO,
        ETC
    }
}
