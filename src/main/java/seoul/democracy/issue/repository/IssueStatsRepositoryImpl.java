package seoul.democracy.issue.repository;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import seoul.democracy.issue.domain.IssueStats;

import static seoul.democracy.issue.domain.QIssueStats.issueStats;

public class IssueStatsRepositoryImpl extends QueryDslRepositorySupport implements IssueStatsRepositoryCustom {

    final private static Expression<Long> constant = Expressions.constant(1L);

    public IssueStatsRepositoryImpl() {
        super(IssueStats.class);
    }

    @Override
    public void increaseViewCount(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.viewCount, issueStats.viewCount.add(constant))
            .execute();
    }

    @Override
    public void selectLikeProposal(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.likeCount, issueStats.likeCount.add(constant))
            .execute();
    }

    @Override
    public void unselectLikeProposal(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.likeCount, issueStats.likeCount.subtract(constant))
            .execute();
    }

    @Override
    public void increaseEtcOpinion(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.etcCount, issueStats.etcCount.add(constant))
            .execute();
    }

    @Override
    public void decreaseEtcOpinion(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.etcCount, issueStats.etcCount.subtract(constant))
            .execute();
    }

    @Override
    public void increaseYesOpinion(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.yesCount, issueStats.yesCount.add(constant))
            .execute();
    }

    @Override
    public void decreaseYesOpinion(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.yesCount, issueStats.yesCount.subtract(constant))
            .execute();
    }

    @Override
    public void increaseNoOpinion(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.noCount, issueStats.noCount.add(constant))
            .execute();
    }

    @Override
    public void decreaseNoOpinion(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.noCount, issueStats.noCount.subtract(constant))
            .execute();
    }

    @Override
    public void increaseApplicant(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.applicantCount, issueStats.applicantCount.add(constant))
            .execute();
    }

    @Override
    public void decreaseApplicant(Long statsId) {
        update(issueStats)
            .where(issueStats.id.eq(statsId))
            .set(issueStats.applicantCount, issueStats.applicantCount.subtract(constant))
            .execute();
    }
}
