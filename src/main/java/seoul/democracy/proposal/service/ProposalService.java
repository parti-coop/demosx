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
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.domain.IssueLike;
import seoul.democracy.issue.predicate.IssueLikePredicate;
import seoul.democracy.issue.repository.CategoryRepository;
import seoul.democracy.issue.repository.IssueLikeRepository;
import seoul.democracy.issue.repository.IssueStatsRepository;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.*;
import seoul.democracy.proposal.repository.ProposalRepository;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.service.UserService;
import seoul.democracy.user.utils.UserUtils;

import static seoul.democracy.issue.predicate.CategoryPredicate.equalName;
import static seoul.democracy.issue.predicate.IssueLikePredicate.equalUserIdAndIssueId;

@Service
@Transactional(readOnly = true)
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final CategoryRepository categoryRepository;
    private final IssueLikeRepository likeRepository;
    private final IssueStatsRepository statsRepository;

    private final UserService userService;

    @Autowired
    public ProposalService(ProposalRepository proposalRepository,
                           CategoryRepository categoryRepository,
                           IssueLikeRepository likeRepository,
                           IssueStatsRepository statsRepository,
                           UserService userService) {
        this.proposalRepository = proposalRepository;
        this.categoryRepository = categoryRepository;
        this.likeRepository = likeRepository;
        this.statsRepository = statsRepository;
        this.userService = userService;
    }

    public ProposalDto getProposal(Predicate predicate, Expression<ProposalDto> projection) {
        return proposalRepository.findOne(predicate, projection);
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
    public Proposal create(ProposalCreateDto createDto, String ip) {
        Proposal proposal = Proposal.create(createDto, ip);
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
    public Proposal delete(Long proposalId, String ip) {
        Proposal proposal = getProposal(proposalId);
        return proposal.delete(ip);
    }

    /**
     * 제안 블럭
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Proposal block(Long proposalId, String ip) {
        Proposal proposal = getProposal(proposalId);
        return proposal.block(ip);
    }

    /**
     * 제안 공개
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Proposal open(Long proposalId, String ip) {
        Proposal proposal = getProposal(proposalId);
        return proposal.open(ip);
    }

    /**
     * 카테고리 변경
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Proposal updateCategory(ProposalCategoryUpdateDto updateDto, String ip) {
        Proposal proposal = getProposal(updateDto.getProposalId());

        if (proposal.getCategory() != null && updateDto.getCategory().equals(proposal.getCategory().getName()))
            return proposal;

        Category category = categoryRepository.findOne(equalName(updateDto.getCategory()));
        if (category == null || !category.getEnabled())
            throw new BadRequestException("category", "error.category", "카테고리를 확인해 주세요.");

        return proposal.updateCategory(category, ip);
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

        IssueLike like = proposal.createLike(user, ip);
        likeRepository.save(like);

        statsRepository.selectLikeProposal(proposal.getStatsId());

        return like;
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
