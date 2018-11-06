package seoul.democracy.feature.E_08_토론;

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
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.predicate.DebatePredicate;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionDto;
import seoul.democracy.opinion.service.OpinionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static seoul.democracy.opinion.dto.OpinionDto.projection;
import static seoul.democracy.opinion.predicate.OpinionPredicate.equalId;


/**
 * epic : 8. 토론
 * story : 8.5 사용자는 토론의견을 삭제할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_8_5_사용자는_토론의견을_삭제할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private DebateService debateService;

    private final Long opinionId = 111L;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 사용자는 본인의 제안의견을 삭제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_1_사용자는_본인의_제안의견을_삭제할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Opinion opinion = opinionService.deleteOpinion(opinionId, ip);

        OpinionDto opinionDto = opinionService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getStatus(), is(Opinion.Status.DELETE));

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(opinion.getIssue().getId()), DebateDto.projection, false, false);
        assertThat(debateDto.getStats().getOpinionCount(), is(0L));
        assertThat(debateDto.getStats().getApplicantCount(), is(0L));
    }

    /**
     * 2. 사용자는 본인의 여러 제안의견 중 하나를 삭제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_2_사용자는_본인의_여러_제안의견_중_하나를_삭제할_수_있다() {
        fail();
    }

    /**
     * 3. 사용자는 본인의 찬성 토론의견을 삭제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_사용자는_본인의_찬성_토론의견을_삭제할_수_있다() {
        fail();
    }

    /**
     * 4. 사용자는 본인의 반대 토론의견을 삭제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_사용자는_본인의_반대_토론의견을_삭제할_수_있다() {
        fail();
    }

    /**
     * 5. 사용자는 본인의 기타 토론의견을 삭제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_사용자는_본인의_기타_토론의견을_삭제할_수_있다() {
        fail();
    }

    /**
     * 6. 다른 사용자의 의견을 삭제할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_6_다른_사용자의_의견을_삭제할_수_없다() {
        fail();
    }

    /**
     * 7. 없는 의견을 삭제할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_7_없는_의견을_삭제할_수_없다() {
        fail();
    }

    /**
     * 8. 삭제된 의견을 삭제할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_8_삭제된_의견을_삭제할_수_없다() {
        fail();
    }

    /**
     * 9. 블럭된 의견을 삭제할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_9_블럭된_의견을_삭제할_수_없다() {
        fail();
    }

}
