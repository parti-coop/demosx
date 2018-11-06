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
import seoul.democracy.proposal.service.ProposalService;

import java.time.format.DateTimeFormatter;

import static org.junit.Assert.fail;


/**
 * epic : 8. 토론
 * story : 8.4 사용자는 토론의견을 수정할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_8_4_사용자는_토론의견을_수정할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private ProposalService proposalService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 사용자는 본인의견을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("user2@googl.co.kr")
    public void T_1_사용자는_본인의견을_수정할_수_있다() {
        fail();
    }

    /**
     * 2. 다른 사용자의 의견을 수정할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_2_다른_사용자의_의견을_수정할_수_없다() {
        fail();
    }

    /**
     * 3. 없는 의견을 수정할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_없는_의견을_수정할_수_없다() {
        fail();
    }

    /**
     * 4. 삭제된 의견을 수정할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_삭제된_의견을_수정할_수_없다() {
        fail();
    }

    /**
     * 5. 블럭된 의견을 수정할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_블럭된_의견을_수정할_수_없다() {
        fail();
    }

}
