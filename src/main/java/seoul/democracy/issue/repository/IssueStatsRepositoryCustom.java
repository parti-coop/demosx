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
     * 기타 의견 증가
     */
    void increaseEtcOpinion(Long statsId);

    /**
     * 기타 의견 감소
     */
    void decreaseEtcOpinion(Long statsId);

    /**
     * 찬성 의견 증가
     */
    void increaseYesOpinion(Long statsId);

    /**
     * 반대 의견 증가
     */
    void increaseNoOpinion(Long statsId);

    /**
     * 참여자 수 증가
     */
    void increaseApplicant(Long statsId);

    /**
     * 참여자 수 감소
     */
    void decreaseApplicant(Long statsId);

}
