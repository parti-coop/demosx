package seoul.democracy.feature.E_06_제안;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.IssueLike;
import seoul.democracy.issue.predicate.IssueLikePredicate;
import seoul.democracy.issue.repository.IssueLikeRepository;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.service.ProposalService;

import java.time.format.DateTimeFormatter;

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

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private IssueLikeRepository likeRepository;


    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 사용자는 제안에 공감할 수 있다.
     */
    @Test
    @WithUserDetails("user2@googl.co.kr")
    public void T_1_사용자는_제안에_공감할_수_있다() {
        Long proposalId = 1L;
        IssueLike like = proposalService.selectLike(proposalId, ip);

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
    public void T_2_사용자는_공감된_제안에_공감해제할_수_있다() {
        Long proposalId = 1L;
        IssueLike like = proposalService.unselectLike(proposalId);

        long count = likeRepository.count(IssueLikePredicate.equalUserId(like.getId().getUserId()));
        assertThat(count, is(2L));

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposalId), projection);
        assertThat(proposalDto.getStats().getLikeCount(), is(0L));
    }

    /**
     * 3. 이미 공감한 제안에 다시 공감할 수 없다.
     */
    @Test(expected = AlreadyExistsException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_이미_공감한_제안에_다시_공감할_수_없다() {
        Long proposalId = 1L;
        proposalService.selectLike(proposalId, ip);
    }

    /**
     * 4. 공감하지 않은 제안에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_4_공감하지_않은_제안에_공감해제할_수_없다() {
        Long proposalId = 1L;
        proposalService.unselectLike(proposalId);
    }

    /**
     * 5. 삭제된 제안에 공감할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_5_삭제된_제안에_공감할_수_없다() {
        Long deleteProposalId = 2L;
        proposalService.selectLike(deleteProposalId, ip);
    }

    /**
     * 6. 블럭된 제안에 공감할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_6_블럭된_제안에_공감할_수_없다() {
        Long blockProposalId = 3L;
        proposalService.selectLike(blockProposalId, ip);
    }

    /**
     * 7. 삭제된 제안에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_7_삭제된_제안에_공감해제할_수_없다() {
        Long deleteProposalId = 2L;
        proposalService.unselectLike(deleteProposalId);
    }

    /**
     * 8. 블럭된 제안에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_8_블럭된_제안에_공감해제할_수_없다() {
        Long blockProposalId = 3L;
        proposalService.unselectLike(blockProposalId);
    }
}
