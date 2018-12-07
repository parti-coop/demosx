package seoul.democracy.proposal.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProposalType {
    PROPOSAL("제안"),   // 제안
    COMPLAINT("민원");   // 민원

    private final String msg;
}
