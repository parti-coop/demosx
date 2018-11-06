package seoul.democracy.issue.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity(name = "TB_ISSUE_STATS")
public class IssueStats {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "STATS_ID")
    private Long id;

    /**
     * 조회수
     */
    @Column(name = "VIEW_CNT", insertable = false, updatable = false)
    private Long viewCount;

    /**
     * 좋아요수
     */
    @Column(name = "LIKE_CNT", insertable = false, updatable = false)
    private Long likeCount;

    /**
     * 참여자수
     */
    @Column(name = "APPLICANT_CNT", insertable = false, updatable = false)
    private Long applicantCount;

    /**
     * 찬성수
     */
    @Column(name = "YES_CNT", insertable = false, updatable = false)
    private Long yesCount;

    /**
     * 반대수
     */
    @Column(name = "NO_CNT", insertable = false, updatable = false)
    private Long noCount;

    /**
     * 기타수
     */
    @Column(name = "ETC_CNT", insertable = false, updatable = false)
    private Long etcCount;

    public static IssueStats create() {
        return new IssueStats();
    }
}
