package seoul.democracy.proposal.service;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.IssueLike;
import seoul.democracy.issue.predicate.IssueLikePredicate;
import seoul.democracy.issue.repository.IssueLikeRepository;
import seoul.democracy.issue.repository.IssueStatsRepository;
import seoul.democracy.issue.service.CategoryService;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.*;
import seoul.democracy.proposal.repository.ProposalRepository;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.service.UserService;
import seoul.democracy.user.utils.UserUtils;

import static seoul.democracy.issue.predicate.IssueLikePredicate.equalUserIdAndIssueId;

@Service
@Transactional(readOnly = true)
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final IssueLikeRepository likeRepository;
    private final IssueStatsRepository statsRepository;

    final private CategoryService categoryService;
    private final UserService userService;


    @Autowired
    public ProposalService(ProposalRepository proposalRepository,
                           IssueLikeRepository likeRepository,
                           IssueStatsRepository statsRepository,
                           CategoryService categoryService,
                           UserService userService) {
        this.proposalRepository = proposalRepository;
        this.likeRepository = likeRepository;
        this.statsRepository = statsRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    public ProposalDto getProposal(Predicate predicate, Expression<ProposalDto> projection) {
        return proposalRepository.findOne(predicate, projection);
    }

    public ProposalDto getProposalWithLiked(Predicate predicate, Expression<ProposalDto> projection) {
        ProposalDto proposal = proposalRepository.findOne(predicate, projection);

        if (proposal == null) return null;

        Long userId = UserUtils.getUserId();
        if (userId == null) return proposal;

        proposal.setLiked(likeRepository.exists(IssueLikePredicate.equalUserIdAndIssueId(userId, proposal.getId())));

        return proposal;
    }

    public Page<ProposalDto> getProposals(Predicate predicate, Pageable pageable, Expression<ProposalDto> projection) {
        return proposalRepository.findAll(predicate, pageable, projection);
    }

    private Proposal getProposal(Long proposalId) {
        Proposal proposal = proposalRepository.findOne(proposalId);
        if (proposal == null) throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        return proposal;
    }

    /**
     * 제안 등록
     */
    @Transactional
    public Proposal create(ProposalCreateDto createDto) {
        Proposal proposal = Proposal.create(createDto);
        return proposalRepository.save(proposal);
    }

    /**
     * 제안 수정
     */
    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id")
    public Proposal update(ProposalUpdateDto updateDto) {
        Proposal proposal = getProposal(updateDto.getId());
        return proposal.update(updateDto);
    }

    /**
     * 제안 삭제
     */
    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id")
    public Proposal delete(Long proposalId) {
        Proposal proposal = getProposal(proposalId);
        return proposal.delete();
    }

    /**
     * 제안 블럭
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Proposal closed(Long proposalId) {
        Proposal proposal = getProposal(proposalId);
        return proposal.block();
    }

    /**
     * 제안 공개
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Proposal open(Long proposalId) {
        Proposal proposal = getProposal(proposalId);
        return proposal.open();
    }

    /**
     * 카테고리 변경
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Proposal updateCategory(ProposalCategoryUpdateDto updateDto) {
        Proposal proposal = getProposal(updateDto.getProposalId());

        return proposal.updateCategory(categoryService.getCategory(updateDto.getCategory()));
    }

    /**
     * 공감
     */
    @Transactional
    public IssueLike selectLike(Long issueId) {
        User user = UserUtils.getLoginUser();
        if (likeRepository.exists(IssueLikePredicate.equalUserIdAndIssueId(user.getId(), issueId)))
            throw new AlreadyExistsException("이미 공감하였습니다.");

        Proposal proposal = getProposal(issueId);

        statsRepository.selectLikeProposal(proposal.getStatsId());

        IssueLike like = proposal.createLike(user);
        likeRepository.save(like);

        return like;
    }

    /**
     * 공감 해제
     */
    @Transactional
    public IssueLike deselectLike(Long issueId) {
        User user = UserUtils.getLoginUser();

        IssueLike like = likeRepository.findOne(equalUserIdAndIssueId(user.getId(), issueId));
        if (like == null)
            throw new BadRequestException("like", "error.like", "공감 상태가 아닙니다.");

        Proposal proposal = getProposal(issueId);
        statsRepository.deselectLikeProposal(proposal.getStatsId());

        proposal.deleteLike();
        likeRepository.delete(like);

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
        User manager = userService.getUser(assignDto.getManagerId());

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
