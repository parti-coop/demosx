package seoul.democracy.proposal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import seoul.democracy.issue.domain.IssueFile;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ProposalCreateDto {

    @NotBlank
    @Size(max = 100)
    private String title;

    private String content;

    private String category;

    private List<IssueFile> files;
}
