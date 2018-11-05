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

    /**
     * 의견 증가
     */
    void increaseOpinion(Long statsId);

    /**
     * 의견 감소
     */
    void decreaseOpinion(Long statsId);

    /**
     * 참여자 수 증가
     */
    void increaseApplicant(Long statsId);

    /**
     * 참여자 수 감소
     */
    void decreaseApplicant(Long statsId);
}
