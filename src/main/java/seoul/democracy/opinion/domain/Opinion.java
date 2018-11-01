package seoul.democracy.opinion.domain;

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

/**
 * 의견
 */
@Getter
@NoArgsConstructor
@Entity(name = "TB_OPINION")
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "OPINION_DTYPE", columnDefinition = "char(1)")
public abstract class Opinion {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "OPINION_ID")
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
    //@Column(name = "REG_ID", insertable = false, updatable = false)
    //private Long createdById;

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
     * 의견 내용
     */
    @Column(name = "OPINION_CONTENT")
    private String content;

    Opinion(Issue issue, String content, String ip) {
        this.issue = issue;
        this.content = content;
        this.createdIp = ip;
        this.modifiedIp = ip;
    }
}
