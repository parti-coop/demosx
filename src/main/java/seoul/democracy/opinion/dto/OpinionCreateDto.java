package seoul.democracy.opinion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import seoul.democracy.opinion.domain.Opinion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class OpinionCreateDto {

    @NotNull
    private Long issueId;

    @NotNull
    private Opinion.Vote vote;

    @NotBlank
    @Size(max = 1000)
    private String content;

    private OpinionCreateDto(Long issueId, String content) {
        this.issueId = issueId;
        this.content = content;
    }

    public static OpinionCreateDto of(Long proposalId, String content) {
        return new OpinionCreateDto(proposalId, content);
    }
}
