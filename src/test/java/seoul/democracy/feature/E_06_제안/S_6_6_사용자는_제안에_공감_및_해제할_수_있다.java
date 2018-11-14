package seoul.democracy.feature.E_06_제안;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.IssueLike;
import seoul.democracy.issue.predicate.IssueLikePredicate;
import seoul.democracy.issue.repository.IssueLikeRepository;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.service.ProposalService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.proposal.dto.ProposalDto.projection;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalId;


/**
 * epic : 6. 제안
 * story : 6.6 사용자는 제안에 공감/해제할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_6_6_사용자는_제안에_공감_및_해제할_수_있다 {

    private final static String ip = "127.0.0.2";

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private IssueLikeRepository likeRepository;

    private final Long proposalIdWith49Like = 61L;
    private final Long proposalIdWith49LikeAssigned = 71L;
    private final Long proposalIdWith49LikeComplete = 81L;
    private final Long proposalIdWith50Like = 31L;
    private final Long proposalIdWith50LikeWithAssigned = 41L;
    private final Long proposalIdWith50LikeWithComplete = 51L;
    private final Long deleteProposalId = 2L;
    private final Long proposalId = 1L;
    private final Long blockProposalId = 3L;

    @Before
    public void setUp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    /**
     * 1. 사용자는 제안에 공감할 수 있다.
     */
    @Test
    @WithUserDetails("user2@googl.co.kr")
    public void T_01_사용자는_제안에_공감할_수_있다() {
        IssueLike like = proposalService.selectLike(proposalId);

        long count = likeRepository.count(IssueLikePredicate.equalUserId(like.getId().getUserId()));
        assertThat(count, is(1L));

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposalId), projection);
        assertThat(proposalDto.getStats().getLikeCount(), is(2L));
    }

    /**
     * 2. 사용자는 공감된 제안에 공감해제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_02_사용자는_공감된_제안에_공감해제할_수_있다() {
        IssueLike like = proposalService.deselectLike(proposalId);

        long count = likeRepository.count(IssueLikePredicate.equalUserId(like.getId().getUserId()));
        assertThat(count, is(5L));

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposalId), projection);
        assertThat(proposalDto.getStats().getLikeCount(), is(0L));
    }

    /**
     * 3. 이미 공감한 제안에 다시 공감할 수 없다.
     */
    @Test(expected = AlreadyExistsException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_03_이미_공감한_제안에_다시_공감할_수_없다() {
        proposalService.selectLike(proposalId);
    }

    /**
     * 4. 공감하지 않은 제안에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_04_공감하지_않은_제안에_공감해제할_수_없다() {
        proposalService.deselectLike(proposalId);
    }

    /**
     * 5. 삭제된 제안에 공감할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_05_삭제된_제안에_공감할_수_없다() {
        proposalService.selectLike(deleteProposalId);
    }

    /**
     * 6. 블럭된 제안에 공감할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_06_블럭된_제안에_공감할_수_없다() {
        proposalService.selectLike(blockProposalId);
    }

    /**
     * 7. 삭제된 제안에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_07_삭제된_제안에_공감해제할_수_없다() {
        proposalService.deselectLike(deleteProposalId);
    }

    /**
     * 8. 블럭된 제안에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_08_블럭된_제안에_공감해제할_수_없다() {
        proposalService.deselectLike(blockProposalId);
    }

    /**
     * 9. 공감수 50인 경우 담당자 지정 가능상태가 된다.
     */
    @Test
    @WithUserDetails("user2@googl.co.kr")
    public void T_09_공감수_50인_경우_담당자_지정_가능상태가_된다() {
        ProposalDto proposalDto = proposalService.getProposal(equalId(proposalIdWith49Like), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.INIT));
        assertThat(proposalDto.getStats().getLikeCount(), is(49L));

        proposalService.selectLike(proposalIdWith49Like);

        proposalDto = proposalService.getProposal(equalId(proposalIdWith49Like), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.NEED_ASSIGN));
        assertThat(proposalDto.getStats().getLikeCount(), is(50L));
    }

    /**
     * 10. 공감수 50인 담당자 지정한 제안의 경우 담당자 지정된 상태로 남아 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_10_공감수_50인_담당자_지정한_제안의_경우_담당자_지정된_상태로_남아_있다() {
        ProposalDto proposalDto = proposalService.getProposal(equalId(proposalIdWith49LikeAssigned), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.ASSIGNED));
        assertThat(proposalDto.getStats().getLikeCount(), is(49L));

        proposalService.selectLike(proposalIdWith49LikeAssigned);

        proposalDto = proposalService.getProposal(equalId(proposalIdWith49LikeAssigned), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.ASSIGNED));
        assertThat(proposalDto.getStats().getLikeCount(), is(50L));
    }

    /**
     * 11. 공감수 50인 답변완료 제안의 경우 답변완료 상태로 남아 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_11_공감수_50인_답변완료_제안의_경우_답변완료_상태로_남아_있다() {
        ProposalDto proposalDto = proposalService.getProposal(equalId(proposalIdWith49LikeComplete), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.COMPLETE));
        assertThat(proposalDto.getStats().getLikeCount(), is(49L));

        proposalService.selectLike(proposalIdWith49LikeComplete);

        proposalDto = proposalService.getProposal(equalId(proposalIdWith49LikeComplete), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.COMPLETE));
        assertThat(proposalDto.getStats().getLikeCount(), is(50L));
    }

    /**
     * 12. 담당자 지정 가능한 제안에 공감해제로 공감수 49인 경우 담당자 지정할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_12_담당자_지정_가능한_제안에_공감해제로_공감수_49인_경우_담당자_지정할_수_없다() {
        ProposalDto proposalDto = proposalService.getProposal(equalId(proposalIdWith50Like), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.NEED_ASSIGN));
        assertThat(proposalDto.getStats().getLikeCount(), is(50L));

        proposalService.deselectLike(proposalIdWith50Like);

        proposalDto = proposalService.getProposal(equalId(proposalIdWith50Like), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.INIT));
        assertThat(proposalDto.getStats().getLikeCount(), is(49L));
    }

    /**
     * 13. 담당자 지정된 제안의 경우 공감해제로 공감수 49인 경우에도 담당자지정 상태가 된다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_13_담당자_지정된_제안의_경우_공감해제로_공감수_49인_경우에도_담당자지정_상태가_된다() {
        ProposalDto proposalDto = proposalService.getProposal(equalId(proposalIdWith50LikeWithAssigned), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.ASSIGNED));
        assertThat(proposalDto.getStats().getLikeCount(), is(50L));

        proposalService.deselectLike(proposalIdWith50LikeWithAssigned);

        proposalDto = proposalService.getProposal(equalId(proposalIdWith50LikeWithAssigned), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.ASSIGNED));
        assertThat(proposalDto.getStats().getLikeCount(), is(49L));
    }

    /**
     * 14. 답변 완료된 제안의 경우 공감해제로 공감수 49인 경우에도 담당자지정 상태가 된다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_14_답변_완료된_제안의_경우_공감해제로_공감수_49인_경우에도_담당자지정_상태가_된다() {
        ProposalDto proposalDto = proposalService.getProposal(equalId(proposalIdWith50LikeWithComplete), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.COMPLETE));
        assertThat(proposalDto.getStats().getLikeCount(), is(50L));

        proposalService.deselectLike(proposalIdWith50LikeWithComplete);

        proposalDto = proposalService.getProposal(equalId(proposalIdWith50LikeWithComplete), projection);
        assertThat(proposalDto.getProcess(), is(Proposal.Process.COMPLETE));
        assertThat(proposalDto.getStats().getLikeCount(), is(49L));
    }
}
