package seoul.democracy.issue.repository;

public interface IssueStatsRepositoryCustom {

    void increaseViewCount(Long statsId);

    /**
     * 제안 공감
     */
    void selectLikeProposal(Long statsId);

    /**
     * 제안 공감 해제
     */
    void unselectLikeProposal(Long statsId);
}
