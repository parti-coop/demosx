package seoul.democracy.issue.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;

import static seoul.democracy.issue.domain.QIssueStats.issueStats;

@Data
public class IssueStatsDto {

    public final static QBean<IssueStatsDto> projection = Projections.fields(IssueStatsDto.class,
        issueStats.viewCount, issueStats.likeCount,
        issueStats.applicantCount,
        issueStats.yesCount, issueStats.noCount, issueStats.etcCount);

    private Long viewCount;
    private Long likeCount;
    private Long applicantCount;
    private Long yesCount;
    private Long noCount;
    private Long etcCount;

    public long getOpinionCount() {
        return yesCount + noCount + etcCount;
    }

    public long yesPercent() {
        long totalCount = getOpinionCount();
        if(totalCount == 0) return 0;
        return yesCount * 100 / totalCount;
    }

    public long noPercent() {
        long totalCount = getOpinionCount();
        if(totalCount == 0) return 0;
        return noCount * 100 / totalCount;
    }

    public long etcPercent() {
        long totalCount = getOpinionCount();
        if(totalCount == 0) return 0;
        return etcCount * 100 / totalCount;
    }

    public String viewLikeCount() {
        return String.format("%,d", likeCount);
    }

    public long likePercentBy500() {
        if(likeCount > 500) return 100;
        if(likeCount == 0) return 0;
        return likeCount * 100 / 500;
    }
}
