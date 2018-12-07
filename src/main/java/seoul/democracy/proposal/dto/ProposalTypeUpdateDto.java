package seoul.democracy.proposal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import seoul.democracy.proposal.domain.ProposalType;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ProposalTypeUpdateDto {

    @NotNull
    private Long proposalId;

    @NotNull
    private ProposalType proposalType;

}
