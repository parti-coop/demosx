package seoul.democracy.opinion.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.democracy.opinion.dto.ProposalOpinionUpdateDto;
import seoul.democracy.proposal.domain.Proposal;

import javax.persistence.*;

/**
 * 제안 의견
 */
@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("P")
public class ProposalOpinion extends Opinion {


    /**
     * 의견 투표
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "OPINION_VOTE", updatable = false)
    private Vote vote;

    private ProposalOpinion(Proposal proposal, String content, String ip) {
        super(proposal, content, ip);
        this.vote = Vote.NONE;
    }

    public static ProposalOpinion create(Proposal proposal, String content, String ip) {
        return new ProposalOpinion(proposal, content, ip);
    }

    @Override
    public ProposalOpinion update(ProposalOpinionUpdateDto updateDto, String ip) {
        this.content = updateDto.getContent();
        this.modifiedIp = ip;
        return this;
    }

    public enum Vote {
        NONE
    }
}
