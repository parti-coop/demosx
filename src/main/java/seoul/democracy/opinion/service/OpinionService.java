package seoul.democracy.opinion.service;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.repository.IssueRepository;
import seoul.democracy.issue.repository.IssueStatsRepository;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.domain.OpinionLike;
import seoul.democracy.opinion.dto.OpinionCreateDto;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.dto.OpinionUpdateDto;
import seoul.democracy.opinion.repository.OpinionLikeRepository;
import seoul.democracy.opinion.repository.OpinionRepository;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.utils.UserUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static seoul.democracy.opinion.predicate.OpinionLikePredicate.equalUserIdAndOpinionId;
import static seoul.democracy.opinion.predicate.OpinionLikePredicate.equalUserIdAndOpinionIdIn;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalIssueIdAndCreatedByIdAndStatus;
import static seoul.democracy.opinion.predicate.OpinionPredicate.orderByIdDesc;

@Service
@Transactional(readOnly = true)
public class OpinionService {

    private final OpinionRepository opinionRepository;
    private final OpinionLikeRepository opinionLikeRepository;
    private final IssueStatsRepository statsRepository;

    private final IssueRepository issueRepository;

    @Autowired
    public OpinionService(OpinionRepository opinionRepository,
                          OpinionLikeRepository opinionLikeRepository,
                          IssueStatsRepository statsRepository,
                          IssueRepository issueRepository) {
        this.opinionRepository = opinionRepository;
        this.opinionLikeRepository = opinionLikeRepository;
        this.statsRepository = statsRepository;
        this.issueRepository = issueRepository;
    }

    public OpinionDto getOpinion(Predicate predicate, Expression<OpinionDto> projection) {
        return opinionRepository.findOne(predicate, projection);
    }

    public Page<OpinionDto> getOpinions(Predicate predicate, Pageable pageable, Expression<OpinionDto> projection) {
        return opinionRepository.findAll(predicate, pageable, projection);
    }

    public Page<OpinionDto> getOpinionsWithLiked(Predicate predicate, Pageable pageable, Expression<OpinionDto> projection) {
        Page<OpinionDto> opinions = opinionRepository.findAll(predicate, pageable, projection);

        Long userId = UserUtils.getUserId();
        if (userId == null) return opinions;

        List<Long> ids = opinions.getContent().stream().map(OpinionDto::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) return opinions;

        Iterable<OpinionLike> likes = opinionLikeRepository.findAll(equalUserIdAndOpinionIdIn(userId, ids));
        Map<Long, OpinionDto> opinionMap = opinions.getContent().stream()
                                               .collect(Collectors.toMap(OpinionDto::getId, identity()));
        for (OpinionLike like : likes) {
            opinionMap.get(like.getId().getOpinionId()).setLiked(true);
        }

        return opinions;
    }

    private Opinion getOpinion(Long opinionId) {
        Opinion opinion = opinionRepository.findOne(opinionId);
        if (opinion == null)
            throw new NotFoundException("의견이 존재하지 않습니다.");
        return opinion;
    }

    private Issue getIssue(Long proposalId) {
        Issue issue = issueRepository.findOne(proposalId);
        if (issue == null || !issue.getStatus().isOpen())
            throw new NotFoundException("해당 제안을 찾을 수 없습니다.");

        return issue;
    }

    /**
     * 해당 issue에 사용자 의견이 있는가?
     */
    private boolean existsOpinion(Long issueId, Long userId) {
        return opinionRepository.exists(equalIssueIdAndCreatedByIdAndStatus(issueId, userId, Opinion.Status.OPEN));
    }

    private OpinionDto latestOpinion(Long issueId, Long userId) {
        //return opinionRepository.findOne
        return opinionRepository.findOne(equalIssueIdAndCreatedByIdAndStatus(issueId, userId, Opinion.Status.OPEN), OpinionDto.projectionForMyOpinion, orderByIdDesc());
    }

    /**
     * 의견 등록
     */
    @Transactional
    public Opinion createOpinion(OpinionCreateDto createDto) {
        Issue issue = getIssue(createDto.getIssueId());

        Opinion opinion = issue.createOpinion(createDto);

        increaseIssueStatsByOpinion(opinion, UserUtils.getUserId());

        return opinionRepository.save(opinion);
    }

    /**
     * 의견 수정
     */
    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id")
    public Opinion updateOpinion(OpinionUpdateDto updateDto) {
        return getOpinion(updateDto.getOpinionId()).update(updateDto);
    }

    /**
     * 의견 삭제
     */
    @Transactional
    @PostAuthorize("returnObject.createdById == authentication.principal.user.id and returnObject.issue.type.name() == 'P'")
    public Opinion deleteOpinion(Long opinionId) {
        Opinion opinion = getOpinion(opinionId);
        opinion.delete();
        opinionRepository.save(opinion);

        decreaseIssueStatsByOpinion(opinion);

        return opinion;
    }

    /**
     * 의견 블럭
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Opinion blockOpinion(Long opinionId) {
        Opinion opinion = getOpinion(opinionId);
        opinion.block();
        opinionRepository.save(opinion);

        decreaseIssueStatsByOpinion(opinion);

        return opinion;
    }

    /**
     * 의견 공개
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Opinion openOpinion(Long opinionId) {
        Opinion opinion = getOpinion(opinionId);

        increaseIssueStatsByOpinion(opinion, opinion.getCreatedById());

        return opinion.open();
    }

    private void increaseVote(Opinion.Vote vote, Long statsId) {
        if (vote == Opinion.Vote.ETC)
            statsRepository.increaseEtcOpinion(statsId);
        else if (vote == Opinion.Vote.YES)
            statsRepository.increaseYesOpinion(statsId);
        else if (vote == Opinion.Vote.NO)
            statsRepository.increaseNoOpinion(statsId);
    }

    private void decreaseVote(Opinion.Vote vote, Long statsId) {
        if (vote == Opinion.Vote.ETC)
            statsRepository.decreaseEtcOpinion(statsId);
        else if (vote == Opinion.Vote.YES)
            statsRepository.decreaseYesOpinion(statsId);
        else if (vote == Opinion.Vote.NO)
            statsRepository.decreaseNoOpinion(statsId);
    }

    private void increaseIssueStatsByOpinion(Opinion opinion, Long userId) {
        Issue issue = opinion.getIssue();
        Long statsId = issue.getStatsId();

        OpinionDto latestOpinion = latestOpinion(issue.getId(), userId);

        // 이전에 의견이 없을 때
        if (latestOpinion == null) {
            statsRepository.increaseApplicant(statsId);
            increaseVote(opinion.getVote(), statsId);
            return;
        }

        // 제안 의견일때
        if (issue.getOpinionType().isProposal()) {
            increaseVote(opinion.getVote(), statsId);
            return;
        }

        // 토론 의견일때
        if (latestOpinion.getVote() == opinion.getVote()) return;

        decreaseVote(latestOpinion.getVote(), statsId);
        increaseVote(opinion.getVote(), statsId);
    }

    private void decreaseIssueStatsByOpinion(Opinion opinion) {
        Issue issue = opinion.getIssue();
        Long statsId = opinion.getIssue().getStatsId();

        OpinionDto latestOpinion = latestOpinion(issue.getId(), opinion.getCreatedById());

        // 이전에 의견이 없을 때
        if (latestOpinion == null) {
            statsRepository.decreaseApplicant(statsId);
            decreaseVote(opinion.getVote(), statsId);
            return;
        }

        // 제안 의견일때
        if (issue.getOpinionType().isProposal()) {
            decreaseVote(opinion.getVote(), statsId);
            return;
        }

        // 토론 의견일때
        // 이전 의견을 삭제한 경우 변경없음
        // 삭제 의견이 최종인 경우 같은 의견이면 변화 없음
        if (latestOpinion.getId() > opinion.getId() || latestOpinion.getVote() == opinion.getVote()) return;

        decreaseVote(opinion.getVote(), statsId);
        increaseVote(latestOpinion.getVote(), statsId);
    }

    /**
     * 의견 공감
     */
    @Transactional
    public OpinionLike selectOpinionLike(Long opinionId) {
        User user = UserUtils.getLoginUser();
        if (opinionLikeRepository.exists(equalUserIdAndOpinionId(user.getId(), opinionId)))
            throw new AlreadyExistsException("이미 공감하였습니다.");

        Opinion opinion = getOpinion(opinionId);

        OpinionLike like = opinion.createLike(user);
        opinionLikeRepository.save(like);

        opinionRepository.increaseLike(opinionId);

        return like;
    }

    /**
     * 의견 공감해제
     */
    @Transactional
    public OpinionLike deselectOpinionLike(Long opinionId) {
        OpinionLike like = opinionLikeRepository.findOne(equalUserIdAndOpinionId(UserUtils.getUserId(), opinionId));
        if (like == null)
            throw new NotFoundException("공감 상태가 아닙니다.");

        Opinion opinion = getOpinion(opinionId);
        if (!opinion.getStatus().isOpen())
            throw new NotFoundException("해당 의견을 찾을 수 없습니다.");

        opinionRepository.decreaseLike(opinion.getId());
        opinionLikeRepository.delete(like);
        return like;
    }

}
