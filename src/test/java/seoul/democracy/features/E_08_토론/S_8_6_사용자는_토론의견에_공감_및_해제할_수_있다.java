package seoul.democracy.features.E_08_토론;

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
import seoul.democracy.opinion.domain.OpinionLike;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.repository.OpinionLikeRepository;
import seoul.democracy.opinion.service.OpinionService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.opinion.dto.OpinionDto.projection;
import static seoul.democracy.opinion.predicate.OpinionLikePredicate.equalUserId;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalId;


/**
 * epic : 8. 토론
 * story : 8.6 사용자는 토론의견에 공감/해제할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_8_6_사용자는_토론의견에_공감_및_해제할_수_있다 {

    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private OpinionLikeRepository likeRepository;

    private final Long opinionId = 111L;
    private final Long likedOpinionId = 151L;

    private final Long deletedOpinionId = 112L;
    private final Long blockedOpinionId = 113L;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    /**
     * 1. 사용자는 의견에 공감할 수 있다.
     */
    @Test
    @WithUserDetails("user2@googl.co.kr")
    public void T_1_사용자는_의견에_공감할_수_있다() {
        OpinionLike like = opinionService.selectOpinionLike(opinionId);

        long count = likeRepository.count(equalUserId(like.getId().getUserId()));
        assertThat(count, is(1L));

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinionId), projection);
        assertThat(opinionDto.getLikeCount(), is(11L));
    }

    /**
     * 2. 사용자는 공감된 의견에 공감해제할 수 있다.
     */
    @Test
    @WithUserDetails("user3@googl.co.kr")
    public void T_2_사용자는_공감된_의견에_공감해제할_수_있다() {
        OpinionLike like = opinionService.deselectOpinionLike(likedOpinionId);

        long count = likeRepository.count(equalUserId(like.getId().getUserId()));
        assertThat(count, is(0L));

        OpinionDto opinionDto = opinionService.getOpinion(equalId(likedOpinionId), OpinionDto.projection);
        assertThat(opinionDto.getLikeCount(), is(0L));
    }

    /**
     * 3. 이미 공감한 의견에 다시 공감할 수 없다.
     */
    @Test(expected = AlreadyExistsException.class)
    @WithUserDetails("user3@googl.co.kr")
    public void T_3_이미_공감한_의견에_다시_공감할_수_없다() {
        opinionService.selectOpinionLike(likedOpinionId);
    }

    /**
     * 4. 공감하지 않은 의견에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_공감하지_않은_의견에_공감해제할_수_없다() {
        opinionService.deselectOpinionLike(opinionId);
    }

    /**
     * 5. 삭제된 의견에 공감할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_삭제된_의견에_공감할_수_없다() {
        opinionService.selectOpinionLike(deletedOpinionId);
    }

    /**
     * 6. 블럭된 의견에 공감할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_6_블럭된_의견에_공감할_수_없다() {
        opinionService.selectOpinionLike(blockedOpinionId);
    }

    /**
     * 7. 삭제된 의견에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user4@googl.co.kr")
    public void T_7_삭제된_의견에_공감해제할_수_없다() {
        opinionService.deselectOpinionLike(deletedOpinionId);
    }

    /**
     * 8. 블럭된 의견에 공감해제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user4@googl.co.kr")
    public void T_8_블럭된_의견에_공감해제할_수_없다() {
        opinionService.deselectOpinionLike(blockedOpinionId);
    }

}
