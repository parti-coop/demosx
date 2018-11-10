package seoul.democracy.issue.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.democracy.common.annotation.CreatedIp;
import seoul.democracy.common.converter.LocalDateTimeAttributeConverter;
import seoul.democracy.common.listener.AuditingIpListener;
import seoul.democracy.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity(name = "TB_ISSUE_LIKE")
@EntityListeners(AuditingIpListener.class)
public class IssueLike {

    @Id
    @AttributeOverrides(value = {
        @AttributeOverride(name = "userId", column = @Column(name = "USER_ID")),
        @AttributeOverride(name = "issueId", column = @Column(name = "ISSUE_ID"))
    })
    private UserIssueId id;

    /**
     * 회원
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false, nullable = false)
    private User user;

    /**
     * 이슈
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ISSUE_ID", insertable = false, updatable = false, nullable = false)
    private Issue issue;

    /**
     * 등록 일시
     */
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "REG_DT", insertable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * 등록 아이피
     */
    @CreatedIp
    @Column(name = "REG_IP", updatable = false)
    private String createdIp;

    private IssueLike(Long userId, Long issueId) {
        this.id = UserIssueId.of(userId, issueId);
    }

    public static IssueLike create(User user, Issue issue) {
        return new IssueLike(user.getId(), issue.getId());
    }
}
