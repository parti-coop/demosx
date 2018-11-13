package seoul.democracy.issue.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

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

    public static List<IssueRelation> create(List<Long> createRelations) {
        if (CollectionUtils.isEmpty(createRelations)) return null;

        List<IssueRelation> relations = new ArrayList<>();
        for (int i = 0; i < createRelations.size(); i++) {
            relations.add(IssueRelation.create(i, createRelations.get(i)));
        }
        return relations;
    }
}
