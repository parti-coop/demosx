package seoul.democracy.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import seoul.democracy.common.annotation.CreatedIp;
import seoul.democracy.common.converter.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 회원 로그인 기록
 */
@Getter
@NoArgsConstructor
@Entity(name = "TB_USER_LOGIN")
public class UserLogin {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "LOGIN_ID")
    private Long id;

    /**
     * 등록 ID
     */
    @Column(name = "USER_ID", updatable = false)
    private Long userId;

    /**
     * 등록 일시
     */
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "REG_DT", updatable = false)
    private LocalDateTime createdDate;

    /**
     * 등록 아이피
     */
    @CreatedIp
    @Column(name = "REG_IP", updatable = false)
    private String createdIp;

    private UserLogin(Long userId, LocalDateTime createdDate, String createdIp) {
        this.userId = userId;
        this.createdDate = createdDate;
        this.createdIp = createdIp;
    }

    public static UserLogin create(Long userId, LocalDateTime createdDate, String createdIp) {
        return new UserLogin(userId, createdDate, createdIp);
    }
}
