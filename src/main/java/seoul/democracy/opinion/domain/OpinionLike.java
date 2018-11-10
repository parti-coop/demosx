package seoul.democracy.opinion.domain;

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
@Entity(name = "TB_OPINION_LIKE")
@EntityListeners(AuditingIpListener.class)
public class OpinionLike {

    @Id
    @AttributeOverrides(value = {
        @AttributeOverride(name = "userId", column = @Column(name = "USER_ID")),
        @AttributeOverride(name = "opinionId", column = @Column(name = "OPINION_ID"))
    })
    private UserOpinionId id;

    /**
     * 회원
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false, nullable = false)
    private User user;

    /**
     * 의견
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OPINION_ID", insertable = false, updatable = false, nullable = false)
    private Opinion opinion;

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

    private OpinionLike(Long userId, Long opinionId) {
        this.id = UserOpinionId.of(userId, opinionId);
    }

    public static OpinionLike create(User user, Opinion opinion) {
        return new OpinionLike(user.getId(), opinion.getId());
    }
}
