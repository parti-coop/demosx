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
    private String category;

    @NotBlank
    @Size(max = 100)
    private String title;

    private String content;

}
