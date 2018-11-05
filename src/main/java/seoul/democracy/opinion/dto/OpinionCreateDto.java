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
    private Long proposalId;

    @NotNull
    private Opinion.Vote vote;

    @NotBlank
    @Size(max = 1000)
    private String content;

    private OpinionCreateDto(Long proposalId, String content) {
        this.proposalId = proposalId;
        this.content = content;
    }

    public static OpinionCreateDto of(Long proposalId, String content) {
        return new OpinionCreateDto(proposalId, content);
    }
}
