package seoul.democracy.history.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class IssueHistoryCreateDto {

    @NotNull
    private Long issueId;

    @NotBlank
    private String content;

}
