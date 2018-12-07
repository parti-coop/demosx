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
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalCategoryUpdateDto;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.predicate.ProposalPredicate;
import seoul.democracy.proposal.service.ProposalService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * epic : 7. 제안관리
 * story : 7.9 관리자는 제안을 분류할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_09_관리자는_제안을_분류할_수_있다 {

    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private ProposalService proposalService;

    private final Long proposalId = 1L;
    private final Long closedProposalId = 21L;
    private final Long deletedProposalId = 11L;

    private ProposalCategoryUpdateDto updateDto;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        updateDto = ProposalCategoryUpdateDto.of(proposalId, "환경");
    }

    /**
     * 1. 관리자는 제안의 카테고리를 변경할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_제안의_카테고리를_변경할_수_있다() {
        Proposal proposal = proposalService.updateCategory(updateDto);

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(proposal.getId()), ProposalDto.projection);
        assertThat(proposalDto.getCategory().getName(), is(updateDto.getCategory()));
    }

    /**
     * 2. 관리자는 비공개 카테고리로 변경할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_관리자는_비공개_카테고리로_변경할_수_없다() {
        updateDto.setCategory("비공개");
        proposalService.updateCategory(updateDto);
    }

    /**
     * 3. 관리자는 없는 카테고리로 변경할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_3_관리자는_없는_카테고리로_변경할_수_없다() {
        updateDto.setCategory("없는 카테고리");
        proposalService.updateCategory(updateDto);
    }

    /**
     * 4. 매니저는 카테고리를 변경할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_4_매니저는_카테고리를_변경할_수_없다() {
        proposalService.updateCategory(updateDto);
    }

    /**
     * 5. 사용자는 카테고리를 변경할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_5_사용자는_카테고리를_변경할_수_없다() {
        proposalService.updateCategory(updateDto);
    }

    /**
     * 6. 비공개된 제안의 카테고리를 변경할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_6_비공개된_제안의_카테고리를_변경할_수_있다() {
        updateDto.setProposalId(closedProposalId);
        Proposal proposal = proposalService.updateCategory(updateDto);

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(proposal.getId()), ProposalDto.projection);
        assertThat(proposalDto.getStatus(), is(Issue.Status.CLOSED));
        assertThat(proposalDto.getCategory().getName(), is(updateDto.getCategory()));
    }

    /**
     * 7. 삭제된 제안의 카테고리를 변경할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_7_삭제된_제안의_카테고리를_변경할_수_있다() {
        updateDto.setProposalId(deletedProposalId);
        Proposal proposal = proposalService.updateCategory(updateDto);

        ProposalDto proposalDto = proposalService.getProposal(ProposalPredicate.equalId(proposal.getId()), ProposalDto.projection);
        assertThat(proposalDto.getStatus(), is(Issue.Status.DELETE));
        assertThat(proposalDto.getCategory().getName(), is(updateDto.getCategory()));
    }
}
