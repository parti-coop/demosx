package seoul.democracy.proposal.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import seoul.democracy.proposal.domain.Proposal;

@Getter
@AllArgsConstructor(staticName = "of")
public class ProposalCreatedEvent {

    private Proposal proposal;

}
