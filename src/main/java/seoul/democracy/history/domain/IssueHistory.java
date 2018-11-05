package seoul.democracy.history.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoul.democracy.common.converter.LocalDateTimeAttributeConverter;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity(name = "TB_ISSUE_HISTORY")
@EntityListeners(AuditingEntityListener.class)
public class IssueHistory {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "HISTORY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ISSUE_ID", updatable = false, nullable = false)
    private Issue issue;

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
    private String createdIp;

    /**
     * 수정 아이피
     */
    @Column(name = "CHG_IP")
    private String modifiedIp;

    /**
     * 히스토리 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "HISTORY_STATUS")
    private Status status;

    /**
     * 히스토리 내용
     */
    @Lob
    @Column(name = "HISTORY_CONTENT")
    private String content;

    private IssueHistory(Issue issue, String content, String ip) {
        this.status = Status.OPEN;
        this.issue = issue;
        this.content = content;
        this.createdIp = this.modifiedIp = ip;
    }

    public static IssueHistory create(Issue issue, String content, String ip) {
        return new IssueHistory(issue, content, ip);
    }

    public enum Status {
        OPEN,
        DELETE
    }
}
