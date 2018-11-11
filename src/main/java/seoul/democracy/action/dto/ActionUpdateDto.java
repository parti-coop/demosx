package seoul.democracy.action.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.dto.IssueFileDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ActionUpdateDto {

    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String thumbnail;

    @NotBlank
    private String category;

    @NotBlank
    @Size(max = 100)
    private String title;

    private String content;

    @NotNull
    private Issue.Status status;

    @Valid
    private List<IssueFileDto> files;

    private List<Long> relations;

    private Map<Long, IssueDto> issueMap;

    public static ActionUpdateDto of(ActionDto actionDto) {
        return of(actionDto.getId(), actionDto.getThumbnail(), actionDto.getCategory().getName(),
            actionDto.getTitle(), actionDto.getContent(), actionDto.getStatus(),
            actionDto.getFiles(), actionDto.getRelations(), actionDto.getIssueMap());
    }
}
