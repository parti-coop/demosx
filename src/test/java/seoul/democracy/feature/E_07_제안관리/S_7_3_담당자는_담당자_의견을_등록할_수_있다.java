package seoul.democracy.feature.E_07_제안관리;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.proposal.service.ProposalService;

import static org.junit.Assert.fail;


/**
 * epic : 7. 제안관리
 * story : 7.3 담당자는 담당자 의견을 등록할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_3_담당자는_담당자_의견을_등록할_수_있다 {

    @Autowired
    private ProposalService proposalService;

    private final Long proposalIdUnder50Like = 1L;
    private final Long proposalIdOver50Like = 4L;

    private final Long adminId = 1L;
    private final Long managerId = 11L;
    private final Long userId = 21L;

    @Before
    public void setUp() throws Exception {

    }

    /**
     * 1. 담당자는 의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_담당자는_의견을_등록할_수_있다() {
        fail();
    }

    /**
     * 2. 담당자가 아닌 관리자는 의견을 등록할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin2@googl.co.kr")
    public void T_2_담당자가_아닌_관리자는_의견을_등록할_수_없다() {
        fail();
    }

    /**
     * 3. 담당자가 아닌 매니저는 의견을 등록할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_3_담당자가_아닌_매니저는_의견을_등록할_수_없다() {
        fail();
    }

    /**
     * 4. 담당자가 아닌 사용자는 의견을 등록할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_4_담당자가_아닌_사용자는_의견을_등록할_수_없다() {
        fail();
    }

    /**
     * 5. 부서답변 상태에서도 의견을 수정할 수 있다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_5_부서답변_상태에서도_의견을_수정할_수_있다() {
        fail();;
    }
}
