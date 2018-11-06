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
 * story : 8.6 사용자는 토론의견에 공감/해제할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_8_6_사용자는_토론의견에_공감_및_해제할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private ProposalService proposalService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 사용자는 의견에 공감할 수 있다.
     */
    @Test
    @WithUserDetails("user2@googl.co.kr")
    public void T_1_사용자는_의견에_공감할_수_있다() {
        fail();
    }

    /**
     * 2. 사용자는 공감된 의견에 공감해제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_2_사용자는_공감된_의견에_공감해제할_수_있다() {
        fail();
    }

    /**
     * 3. 이미 공감한 의견에 다시 공감할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_이미_공감한_의견에_다시_공감할_수_없다() {
        fail();
    }

    /**
     * 4. 공감하지 않은 의견에 공감해제할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_공감하지_않은_의견에_공감해제할_수_없다() {
        fail();
    }

    /**
     * 5. 삭제된 의견에 공감할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_삭제된_의견에_공감할_수_없다() {
        fail();
    }

    /**
     * 6. 블럭된 의견에 공감할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_6_블럭된_의견에_공감할_수_없다() {
        fail();
    }

    /**
     * 7. 삭제된 의견에 공감해제할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_7_삭제된_의견에_공감해제할_수_없다() {
        fail();
    }

    /**
     * 8. 블럭된 의견에 공감해제할 수 없다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_8_블럭된_의견에_공감해제할_수_없다() {
        fail();
    }

}
