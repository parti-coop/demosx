package seoul.democracy.proposal.service;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.domain.IssueLike;
import seoul.democracy.issue.predicate.CategoryPredicate;
import seoul.democracy.issue.predicate.IssueLikePredicate;
import seoul.democracy.issue.repository.CategoryRepository;
import seoul.democracy.issue.repository.IssueLikeRepository;
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
import seoul.democracy.proposal.dto.*;
import seoul.democracy.proposal.repository.ProposalRepository;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.predicate.UserPredicate;
import seoul.democracy.user.service.UserService;
import seoul.democracy.user.utils.UserUtils;

import static seoul.democracy.issue.predicate.IssueLikePredicate.equalUserIdAndIssueId;
import static seoul.democracy.opinion.predicate.OpinionLikePredicate.equalUserIdAndOpinionId;

@Service
@Transactional(readOnly = true)
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final CategoryRepository categoryRepository;
    private final IssueLikeRepository likeRepository;
    private final IssueStatsRepository statsRepository;

    private final OpinionRepository opinionRepository;
    private final OpinionLikeRepository opinionLikeRepository;

    private final UserService userService;

    @Autowired
    public ProposalService(ProposalRepository proposalRepository,
                           CategoryRepository categoryRepository,
                           IssueLikeRepository likeRepository,
                           IssueStatsRepository statsRepository,
                           OpinionRepository opinionRepository,
                           OpinionLikeRepository opinionLikeRepository,
                           UserService userService) {
        this.proposalRepository = proposalRepository;
        this.categoryRepository = categoryRepository;
        this.likeRepository = likeRepository;
        this.statsRepository = statsRepository;
        this.opinionRepository = opinionRepository;
        this.opinionLikeRepository = opinionLikeRepository;
        this.userService = userService;
    }

    public ProposalDto getProposal(Predicate predicate, Expression<ProposalDto> projection) {
        return proposalRepository.findOne(predicate, projection);
    }

    private Proposal getProposal(Long proposalId) {
        Proposal proposal = proposalRepository.findOne(proposalId);
        if (proposal == null || proposal.getStatus().isDelete() || proposal.getStatus().isBlock())
            throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        return proposal;
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

    /**
     * 제안 등록
     */
    @Transactional
    public Proposal create(ProposalCreateDto createDto, String ip) {
        Category category = categoryRepository.findOne(CategoryPredicate.equalName(createDto.getCategory()));
        if (category == null || !category.getEnabled())
            throw new BadRequestException("category", "error.category", "카테고리를 확인해 주세요.");

        Proposal proposal = Proposal.create(createDto, category, ip);

        return proposalRepository.save(proposal);
    }

    /**
     * 제안 수정
     */
    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id")
    public Proposal update(ProposalUpdateDto updateDto, String ip) {
        Proposal proposal = getProposal(updateDto.getId());
        return proposal.update(updateDto, ip);
    }

    /**
     * 제안 삭제
     */
    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id")
    public Proposal delete(Long id, String ip) {
        Proposal proposal = getProposal(id);
        return proposal.delete(ip);
    }

    /**
     * 공감
     */
    @Transactional
    public IssueLike selectLike(Long issueId, String ip) {
        User user = UserUtils.getLoginUser();
        if (likeRepository.exists(IssueLikePredicate.equalUserIdAndIssueId(user.getId(), issueId)))
            throw new AlreadyExistsException("이미 공감하였습니다.");

        Proposal proposal = getProposal(issueId);
        statsRepository.selectLikeProposal(proposal.getStatsId());

        IssueLike like = IssueLike.create(user, proposal, ip);
        return likeRepository.save(like);
    }

    /**
     * 공감 해제
     */
    @Transactional
    public IssueLike unselectLike(Long issueId) {
        User user = UserUtils.getLoginUser();

        IssueLike like = likeRepository.findOne(equalUserIdAndIssueId(user.getId(), issueId));
        if (like == null)
            throw new NotFoundException("공감 상태가 아닙니다.");

        Proposal proposal = getProposal(issueId);
        statsRepository.unselectLikeProposal(proposal.getStatsId());
        likeRepository.delete(like);

        return like;
    }

    /**
     * 의견 등록
     */
    @Transactional
    public ProposalOpinion createOpinion(ProposalOpinionCreateDto createDto, String ip) {
        Proposal proposal = getProposal(createDto.getProposalId());

        ProposalOpinion opinion = proposal.createOpinion(createDto.getContent(), ip);

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
        return getOpinion(opinionId).delete(ip);
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
        opinionRepository.selectLike(opinionId);

        OpinionLike like = OpinionLike.create(user, opinion, ip);
        return opinionLikeRepository.save(like);
    }

    /**
     * 의견 공감해제
     */
    @Transactional
    public OpinionLike unselectOpinionLike(Long opinionId) {
        User user = UserUtils.getLoginUser();

        OpinionLike like = opinionLikeRepository.findOne(equalUserIdAndOpinionId(user.getId(), opinionId));
        if (like == null)
            throw new NotFoundException("공감 상태가 아닙니다.");

        Opinion opinion = getOpinion(opinionId);
        opinionRepository.unselectLike(opinion.getId());
        opinionLikeRepository.delete(like);
        return like;
    }

    /**
     * 관리자 답변
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Proposal editAdminComment(ProposalAdminCommentEditDto editDto) {
        Proposal proposal = getProposal(editDto.getProposalId());
        return proposal.editAdminComment(editDto.getComment());
    }

    /**
     * 담당자 할당
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Proposal assignManager(ProposalManagerAssignDto assignDto) {
        Proposal proposal = getProposal(assignDto.getProposalId());
        if (proposal.getStats().getLikeCount() < 50)
            throw new BadRequestException("likeCount", "error.likeCount", "공감수 50이상 제안만 담당자 지정이 가능합니다.");

        User manager = userService.getUser(UserPredicate.equalId(assignDto.getManagerId()));
        if (!manager.getRole().isManager())
            throw new BadRequestException("role", "error.role", "매니저만 담당자로 설정가능합니다.");

        return proposal.assignManager(manager);
    }

    /**
     * 담당자 답변
     */
    @Transactional
    @PostAuthorize("returnObject.managerId == authentication.principal.user.id")
    public Proposal editManagerComment(ProposalManagerCommentEditDto editDto) {
        Proposal proposal = getProposal(editDto.getProposalId());
        return proposal.editManagerComment(editDto.getComment());
    }
}
