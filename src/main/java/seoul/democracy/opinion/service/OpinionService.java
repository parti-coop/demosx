package seoul.democracy.opinion.service;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.repository.IssueStatsRepository;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.domain.OpinionLike;
import seoul.democracy.opinion.domain.ProposalOpinion;
import seoul.democracy.opinion.dto.ProposalOpinionCreateDto;
import seoul.democracy.opinion.dto.ProposalOpinionDto;
import seoul.democracy.opinion.dto.ProposalOpinionUpdateDto;
import seoul.democracy.opinion.repository.OpinionLikeRepository;
import seoul.democracy.opinion.repository.OpinionRepository;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.repository.ProposalRepository;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.utils.UserUtils;

import static seoul.democracy.opinion.predicate.OpinionLikePredicate.equalUserIdAndOpinionId;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalIssueIdAndCreatedByIdAndStatus;

@Service
@Transactional(readOnly = true)
public class OpinionService {

    private final OpinionRepository opinionRepository;
    private final OpinionLikeRepository opinionLikeRepository;
    private final IssueStatsRepository statsRepository;

    private final ProposalRepository proposalRepository;

    @Autowired
    public OpinionService(OpinionRepository opinionRepository,
                          OpinionLikeRepository opinionLikeRepository,
                          IssueStatsRepository statsRepository,
                          ProposalRepository proposalRepository) {
        this.opinionRepository = opinionRepository;
        this.opinionLikeRepository = opinionLikeRepository;
        this.statsRepository = statsRepository;
        this.proposalRepository = proposalRepository;
    }

    public ProposalOpinionDto getOpinion(Predicate predicate, Expression<ProposalOpinionDto> projection) {
        return opinionRepository.findOne(predicate, projection);
    }

    private Opinion getOpinion(Long opinionId) {
        Opinion opinion = opinionRepository.findOne(opinionId);
        if (opinion == null || opinion.getStatus().isDelete() || opinion.getStatus().isBlock())
            throw new NotFoundException("의견이 존재하지 않습니다.");
        return opinion;
    }

    private Proposal getProposal(Long proposalId) {
        Proposal proposal = proposalRepository.findOne(proposalId);
        if (proposal == null || proposal.getStatus().isDelete() || proposal.getStatus().isBlock())
            throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        return proposal;
    }

    /**
     * 해당 issue에 로그인 사용자 의견이 있는가?
     */
    private boolean existsOpinion(Long issueId, Long userId) {
        return opinionRepository.exists(equalIssueIdAndCreatedByIdAndStatus(issueId, userId, Opinion.Status.OPEN));
    }

    /**
     * 의견 등록
     */
    @Transactional
    public ProposalOpinion createOpinion(ProposalOpinionCreateDto createDto, String ip) {
        Proposal proposal = getProposal(createDto.getProposalId());

        ProposalOpinion opinion = proposal.createOpinion(createDto.getContent(), ip);
        statsRepository.increaseOpinion(proposal.getStatsId());

        if (!existsOpinion(proposal.getId(), UserUtils.getUserId()))
            statsRepository.increaseApplicant(proposal.getStatsId());

        return opinionRepository.save(opinion);
    }

    /**
     * 의견 수정
     */
    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id")
    public ProposalOpinion updateOpinion(ProposalOpinionUpdateDto updateDto, String ip) {
        return getOpinion(updateDto.getOpinionId()).update(updateDto, ip);
    }

    /**
     * 의견 삭제
     */
    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id")
    public Opinion deleteOpinion(Long opinionId, String ip) {
        Opinion opinion = getOpinion(opinionId);
        opinion.delete(ip);
        opinionRepository.save(opinion);

        statsRepository.decreaseOpinion(opinion.getIssue().getStatsId());

        if (!existsOpinion(opinion.getIssue().getId(), opinion.getCreatedById()))
            statsRepository.decreaseApplicant(opinion.getIssue().getStatsId());

        return opinion;
    }

    /**
     * 의견 블럭
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Opinion blockOpinion(Long opinionId, String ip) {
        Opinion opinion = getOpinion(opinionId);
        opinion.block(ip);
        opinionRepository.save(opinion);

        statsRepository.decreaseOpinion(opinion.getIssue().getStatsId());

        if (!existsOpinion(opinion.getIssue().getId(), opinion.getCreatedById()))
            statsRepository.decreaseApplicant(opinion.getIssue().getStatsId());

        return opinion;
    }

    /**
     * 의견 공감
     */
    @Transactional
    public OpinionLike selectOpinionLike(Long opinionId, String ip) {
        User user = UserUtils.getLoginUser();
        if (opinionLikeRepository.exists(equalUserIdAndOpinionId(user.getId(), opinionId)))
            throw new AlreadyExistsException("이미 공감하였습니다.");

        Opinion opinion = getOpinion(opinionId);
        opinionRepository.increaseLike(opinionId);

        OpinionLike like = OpinionLike.create(user, opinion, ip);
        return opinionLikeRepository.save(like);
    }

    /**
     * 의견 공감해제
     */
    @Transactional
    public OpinionLike unselectOpinionLike(Long opinionId) {
        OpinionLike like = opinionLikeRepository.findOne(equalUserIdAndOpinionId(UserUtils.getUserId(), opinionId));
        if (like == null)
            throw new NotFoundException("공감 상태가 아닙니다.");

        Opinion opinion = getOpinion(opinionId);
        opinionRepository.decreaseLike(opinion.getId());
        opinionLikeRepository.delete(like);
        return like;
    }
}
