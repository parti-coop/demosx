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
@DiscriminatorValue("P")
public class ProposalOpinion extends Opinion {


    private ProposalOpinion(Issue issue, String content, String ip) {
        super(issue, content, ip);
        this.vote = Vote.ETC;
    }

    public static ProposalOpinion create(Issue issue, String content, String ip) {
        return new ProposalOpinion(issue, content, ip);
    }
}
