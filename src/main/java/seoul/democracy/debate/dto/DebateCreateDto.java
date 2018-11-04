package seoul.democracy.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.opinion.domain.OpinionType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class DebateCreateDto {

    @NotBlank
    @Size(max = 100)
    private String thumbnail;

    @NotBlank
    private String category;

    @NotNull
    private OpinionType opinionType;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotBlank
    @Size(max = 100)
    private String title;

    private String content;

    @NotNull
    private Issue.Status status;

    @Valid
    private List<IssueFileDto> files;

    private List<Long> relations;

}
