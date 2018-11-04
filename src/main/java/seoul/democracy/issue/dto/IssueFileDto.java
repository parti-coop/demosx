package seoul.democracy.issue.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

import static seoul.democracy.issue.domain.QIssueFile.issueFile;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class IssueFileDto {

    public final static QBean<IssueFileDto> projection = Projections.fields(IssueFileDto.class,
        issueFile.name, issueFile.url);

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String url;


}
