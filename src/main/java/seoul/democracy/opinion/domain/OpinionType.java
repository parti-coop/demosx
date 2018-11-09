package seoul.democracy.opinion.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OpinionType {
    PROPOSAL("제안의견"),   // 제안의견
    DEBATE("투표의견");      // 토론의견

    private final String msg;

    public boolean isProposal() {
        return this == PROPOSAL;
    }

    public boolean isDebate() {
        return this == DEBATE;
    }
}
