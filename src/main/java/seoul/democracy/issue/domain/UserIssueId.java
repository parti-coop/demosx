package seoul.democracy.issue.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserIssueId implements Serializable {

    private Long userId;

    private Long issueId;

}
