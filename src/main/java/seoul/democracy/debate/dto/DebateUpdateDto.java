package seoul.democracy.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.dto.IssueFileDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class DebateUpdateDto {

    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String thumbnail;

    @NotBlank
    private String category;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    public static DebateUpdateDto of(DebateDto debateDto) {
        return of(debateDto.getId(), debateDto.getThumbnail(), debateDto.getCategory().getName(),
            debateDto.getStartDate(), debateDto.getEndDate(),
            debateDto.getTitle(), debateDto.getContent(), debateDto.getStatus(),
            debateDto.getFiles(), debateDto.getRelations());
    }
}
