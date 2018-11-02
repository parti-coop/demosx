package seoul.democracy.proposal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ProposalManagerCommentEditDto {

    @NotNull
    private Long proposalId;

    @NotBlank
    private String comment;

}
