package seoul.democracy.opinion.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.democracy.issue.domain.Issue;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 제안 의견
 */
@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("D")
public class DebateOpinion extends Opinion {

    private DebateOpinion(Issue issue, Vote vote, String content, String ip) {
        super(issue, content, ip);
        this.vote = vote;
    }

    public static DebateOpinion create(Issue issue, Vote vote, String content, String ip) {
        return new DebateOpinion(issue, vote, content, ip);
    }
}
