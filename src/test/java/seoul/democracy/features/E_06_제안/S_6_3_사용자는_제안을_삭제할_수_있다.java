package seoul.democracy.features.E_06_제안;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.service.ProposalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.proposal.dto.ProposalDto.projection;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalId;


/**
 * epic : 6. 제안
 * story : 6.3 사용자는 제안을 삭제할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_6_3_사용자는_제안을_삭제할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private ProposalService proposalService;


    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    /**
     * 1. 사용자는 제안을 삭제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_1_사용자는_제안을_삭제할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Proposal proposal = proposalService.delete(1L);

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getModifiedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(proposalDto.getModifiedIp(), is(ip));

        assertThat(proposalDto.getStatus(), is(Issue.Status.DELETE));
    }

    /**
     * 2. 다른 사람의 제안을 삭제할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_2_다른_사람의_제안을_삭제할_수_없다() {
        proposalService.delete(1L);
    }

    /**
     * 3. 존재하지 않는 제안을 삭제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_존재하지_않는_제안을_삭제할_수_없다() {
        Long notExistsId = 999L;
        proposalService.delete(notExistsId);
    }

    /**
     * 4. 삭제된 제안을 삭제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_삭제된_제안을_삭제할_수_없다() {
        proposalService.delete(2L);
    }

    /**
     * 5. 블럭된 제안을 삭제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_블럭된_제안을_삭제할_수_없다() {
        proposalService.delete(3L);
    }
}
