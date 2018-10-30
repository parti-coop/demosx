package seoul.democracy.issue.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;

import static seoul.democracy.issue.domain.QIssueStats.issueStats;

@Data
public class IssueStatsDto {

    public final static QBean<IssueStatsDto> projection = Projections.fields(IssueStatsDto.class,
        issueStats.viewCount, issueStats.likeCount, issueStats.opinionCount,
        issueStats.yesCount, issueStats.noCount, issueStats.etcCount);

    private Long viewCount;
    private Long likeCount;
    private Long opinionCount;
    private Long yesCount;
    private Long noCount;
    private Long etcCount;
}
