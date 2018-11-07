package seoul.democracy.proposal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ProposalAdminCommentEditDto {

    @NotNull
    private Long proposalId;

    private String comment;

}
