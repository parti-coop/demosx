package seoul.democracy.feature;

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
import seoul.democracy.opinion.domain.OpinionLike;
import seoul.democracy.opinion.dto.ProposalOpinionDto;
import seoul.democracy.opinion.predicate.ProposalOpinionPredicate;
import seoul.democracy.opinion.repository.OpinionLikeRepository;
import seoul.democracy.proposal.service.ProposalService;

import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.opinion.predicate.OpinionLikePredicate.equalUserId;


/**
 * epic : 6. 제안
 * story : 6.9 사용자는 제안의견에 공감/해제할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_6_9_사용자는_제안의견에_공감_및_해제할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private OpinionLikeRepository likeRepository;

    private Long opinionId = 1L;
    private Long deletedOpinionId = 2L;
    private Long blockedOpinionId = 3L;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 사용자는 제안에 공감할 수 있다.
     */
    @Test
    @WithUserDetails("user2@googl.co.kr")
    public void T_1_사용자는_제안의견에_공감할_수_있다() {

        OpinionLike like = proposalService.selectOpinionLike(opinionId, ip);

        long count = likeRepository.count(equalUserId(like.getId().getUserId()));
        assertThat(count, is(1L));

        ProposalOpinionDto opinionDto = proposalService.getOpinion(ProposalOpinionPredicate.equalId(opinionId), ProposalOpinionDto.projection);
        assertThat(opinionDto.getLikeCount(), is(2L));
    }

    /**
     * 2. 사용자는 공감된 제안의견에 공감해제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_2_사용자는_공감된_제안의견에_공감해제할_수_있다() {
        OpinionLike like = proposalService.unselectOpinionLike(opinionId);

        long count = likeRepository.count(equalUserId(like.getId().getUserId()));
        assertThat(count, is(2L));

        ProposalOpinionDto opinionDto = proposalService.getOpinion(ProposalOpinionPredicate.equalId(opinionId), ProposalOpinionDto.projection);
        assertThat(opinionDto.getLikeCount(), is(0L));
    }

    /**
     * 3. 이미 공감한 제안의견에 다시 공감할 수 없다.
     */
    @Test(expected = AlreadyExistsException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_이미_공감한_제안의견에_다시_공감할_수_없다() {
        proposalService.selectOpinionLike(opinionId, ip);
    }

    /**
     * 4. 공감하지 않은 제안의견에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_4_공감하지_않은_제안의견에_공감해제할_수_없다() {
        proposalService.unselectOpinionLike(opinionId);
    }

    /**
     * 5. 삭제된 제안의견에 공감할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_5_삭제된_제안의견에_공감할_수_없다() {
        proposalService.selectOpinionLike(deletedOpinionId, ip);
    }

    /**
     * 6. 블럭된 제안의견에 공감할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_6_블럭된_제안의견에_공감할_수_없다() {
        proposalService.selectOpinionLike(blockedOpinionId, ip);
    }

    /**
     * 7. 삭제된 제안의견에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_7_삭제된_제안의견에_공감해제할_수_없다() {
        proposalService.unselectOpinionLike(deletedOpinionId);
    }

    /**
     * 8. 블럭된 제안에 공감의견해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_8_블럭된_제안의견에_공감해제할_수_없다() {
        proposalService.unselectOpinionLike(blockedOpinionId);
    }
}
