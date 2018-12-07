package seoul.democracy.features.E_07_제안관리;

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
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.domain.ProposalType;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.dto.ProposalTypeUpdateDto;
import seoul.democracy.proposal.predicate.ProposalPredicate;
import seoul.democracy.proposal.service.ProposalService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * epic : 7. 제안관리
 * story : 7.10 관리자는 제안타입을 설정할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_10_관리자는_제안타입을_설정할_수_있다 {

    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private ProposalService proposalService;

    private final Long proposalId = 1L;
    private ProposalTypeUpdateDto updateDto;


    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        updateDto = ProposalTypeUpdateDto.of(proposalId, ProposalType.PROPOSAL);
    }

    /**
     * 1. 관리자는 제안타입으로 설정할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_제안타입으로_설정할_수_있다() {
        Proposal proposal = proposalService.updateProposalType(updateDto);

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(proposal.getId()), ProposalDto.projection);
        assertThat(proposalDto.getProposalType(), is(updateDto.getProposalType()));
    }

    /**
     * 2. 관리자는 민원타입으로 설정할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_관리자는_민원타입으로_설정할_수_있다() {
        updateDto.setProposalType(ProposalType.COMPLAINT);
        Proposal proposal = proposalService.updateProposalType(updateDto);

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(proposal.getId()), ProposalDto.projection);
        assertThat(proposalDto.getProposalType(), is(updateDto.getProposalType()));
    }

    /**
     * 3. 매니저는 제안타입을 설정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_3_매니저는_제안타입을_설정할_수_없다() {
        proposalService.updateProposalType(updateDto);
    }

    /**
     * 4. 사용자는 제안타입을 설정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_사용자는_제안타입을_설정할_수_없다() {
        proposalService.updateProposalType(updateDto);
    }

}
