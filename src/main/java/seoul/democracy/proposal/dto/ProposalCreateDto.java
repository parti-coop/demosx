package seoul.democracy.proposal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ProposalCreateDto {

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String content;

}
