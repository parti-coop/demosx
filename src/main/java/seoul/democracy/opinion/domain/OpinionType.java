package seoul.democracy.opinion.domain;

public enum OpinionType {
    PROPOSAL,   // 제안의견
    DEBATE;      // 토론의견

    public boolean isProposal() {
        return this == PROPOSAL;
    }

    public boolean isDebate() {
        return this == DEBATE;
    }
}
