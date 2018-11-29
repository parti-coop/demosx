package seoul.democracy.stats.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoul.democracy.common.converter.LocalDateAttributeConverter;
import seoul.democracy.common.converter.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 통계이슈
 */
@Getter
@NoArgsConstructor
@Entity(name = "TB_STATS_ISSUE")
@EntityListeners(AuditingEntityListener.class)
public class StatsIssue {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "STATS_ID")
    private Long id;

    /**
     * 등록 일시
     */
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "REG_DT", insertable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * 통계 일자
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "STATS_DATE", updatable = false)
    private LocalDate date;

    /**
     * 제안 수
     */
    @Column(name = "PROPOSAL_CNT", updatable = false)
    private Long proposalCount;

    /**
     * 토론 수
     */
    @Column(name = "DEBATE_CNT", updatable = false)
    private Long debateCount;

    /**
     * 실행 수
     */
    @Column(name = "ACTION_CNT", updatable = false)
    private Long actionCount;

    /**
     * 기관 제안 수
     */
    @Column(name = "ORG_DEBATE_CNT", updatable = false)
    private Long orgDebateCount;

    private StatsIssue(LocalDate date, Long proposalCount, Long debateCount, Long actionCount, Long orgDebateCount) {
        this.date = date;
        this.proposalCount = proposalCount;
        this.debateCount = debateCount;
        this.actionCount = actionCount;
        this.orgDebateCount = orgDebateCount;
    }

    public static StatsIssue create(LocalDate date, Long proposalCount, Long debateCount, Long actionCount, Long orgDebateCount) {
        return new StatsIssue(date, proposalCount, debateCount, actionCount, orgDebateCount);
    }
}
