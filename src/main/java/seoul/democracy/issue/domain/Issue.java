package seoul.democracy.issue.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoul.democracy.common.converter.LocalDateTimeAttributeConverter;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.history.domain.IssueHistory;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.opinion.dto.OpinionCreateDto;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 주제 Entity
 */
@Getter
@NoArgsConstructor
@Entity(name = "TB_ISSUE")
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ISSUE_DTYPE", columnDefinition = "char(1)")
public abstract class Issue {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ISSUE_ID")
    private Long id;

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
    @Column(name = "REG_IP", updatable = false)
    protected String createdIp;

    /**
     * 수정 아이피
     */
    @Column(name = "CHG_IP")
    protected String modifiedIp;

    /**
     * 의견 타입
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "OPINION_TYPE", updatable = false)
    protected OpinionType opinionType;

    /**
     * 이슈 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ISSUE_STATUS")
    protected Status status;

    /**
     * 범주
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATE_ID")
    protected Category category;

    /**
     * 통계
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "STATS_ID", updatable = false, nullable = false)
    protected IssueStats stats;
    @Column(name = "STATS_ID", insertable = false, updatable = false)
    private Long statsId;

    /**
     * 이슈 파일
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "TB_ISSUE_FILE", joinColumns = @JoinColumn(name = "ISSUE_ID"))
    protected List<IssueFile> files;

    /**
     * 이슈 제목
     */
    @Column(name = "ISSUE_TITLE")
    protected String title;

    /**
     * 이슈 내용
     */
    @Lob
    @Column(name = "ISSUE_CONTENT")
    protected String content;

    public IssueHistory createHistory(String content, String ip) {
        if (this instanceof Proposal)
            throw new BadRequestException("issueId", "error.issueId", "제안은 히스토리를 등록할 수 없습니다.");

        return IssueHistory.create(this, content, ip);
    }

    public abstract Opinion createOpinion(OpinionCreateDto createDto, String ip);

    public abstract boolean isUpdatableOpinion();

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        OPEN("공개"),       // 공개
        CLOSED("비공개"),     // 비공개, 제안 비공개
        DELETE("삭제");     // 삭제

        private final String msg;

        public boolean isOpen() {
            return this == OPEN;
        }

        public boolean isClosed() {
            return this == CLOSED;
        }

        public boolean isDelete() {
            return this == DELETE;
        }
    }
}
