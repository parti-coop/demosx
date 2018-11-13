package seoul.democracy.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.opinion.domain.OpinionType;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 100)
    private String excerpt;

    private String content;

    @NotNull
    private Issue.Status status;

    @Valid
    private List<IssueFileDto> files;

    private List<Long> relations;

    private Map<Long, IssueDto> issueMap;

    @AssertTrue(message = "토론 일시를 확인해 주세요.")
    public boolean isValidDate() {
        if (startDate == null || endDate == null) return true;
        return startDate.isEqual(endDate) || startDate.isBefore(endDate);
    }

    public String period() {
        if (startDate == null || endDate == null) return "";
        return startDate.toString() + " - " + endDate.toString();
    }
}
