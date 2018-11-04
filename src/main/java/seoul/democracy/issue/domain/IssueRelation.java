package seoul.democracy.issue.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 이슈 연관
 */
@Getter
@Embeddable
@NoArgsConstructor
public class IssueRelation {

    /**
     * 연관 순번
     */
    @Column(name = "RELATION_SEQ")
    private Integer seq;

    /**
     * 연관
     */
    @Column(name = "RELATION_ID")
    private Long issueId;

    private IssueRelation(Integer seq, Long issueId) {
        this.seq = seq;
        this.issueId = issueId;
    }

    public static IssueRelation create(Integer seq, Long issueId) {
        return new IssueRelation(seq, issueId);
    }
}
