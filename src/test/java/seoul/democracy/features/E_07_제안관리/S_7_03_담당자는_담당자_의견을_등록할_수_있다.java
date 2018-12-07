package seoul.democracy.features.E_07_제안관리;

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
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.dto.ProposalManagerCommentEditDto;
import seoul.democracy.proposal.service.ProposalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.proposal.dto.ProposalDto.projection;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalId;


/**
 * epic : 7. 제안관리
 * story : 7.3 담당자는 담당자 의견을 등록할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_7_03_담당자는_담당자_의견을_등록할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");

    @Autowired
    private ProposalService proposalService;

    private final Long assignedProposalId = 41L;
    private final Long completedProposalId = 51L;

    @Before
    public void setUp() throws Exception {

    }

    /**
     * 1. 담당자는 의견을 등록할 수 있다.
     */
    @Test
    @WithUserDetails("manager1@googl.co.kr")
    public void T_1_담당자는_의견을_등록할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        ProposalManagerCommentEditDto editDto = ProposalManagerCommentEditDto.of(assignedProposalId, "담당자 답변입니다.");
        Proposal proposal = proposalService.editManagerComment(editDto);

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getManagerCommentDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getManagerComment(), is(editDto.getComment()));
        assertThat(proposalDto.getProcess(), is(Proposal.Process.COMPLETE));
    }

    /**
     * 2. 담당자가 아닌 관리자는 의견을 등록할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_담당자가_아닌_관리자는_의견을_등록할_수_없다() {
        ProposalManagerCommentEditDto editDto = ProposalManagerCommentEditDto.of(assignedProposalId, "담당자가 아닌 관리자는 답변할 수 없습니다.");
        proposalService.editManagerComment(editDto);
    }

    /**
     * 3. 담당자가 아닌 매니저는 의견을 등록할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager2@googl.co.kr")
    public void T_3_담당자가_아닌_매니저는_의견을_등록할_수_없다() {
        ProposalManagerCommentEditDto editDto = ProposalManagerCommentEditDto.of(assignedProposalId, "담당자가 아닌 매니저는 답변할 수 없습니다.");
        proposalService.editManagerComment(editDto);
    }

    /**
     * 4. 담당자가 아닌 사용자는 의견을 등록할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_담당자가_아닌_사용자는_의견을_등록할_수_없다() {
        ProposalManagerCommentEditDto editDto = ProposalManagerCommentEditDto.of(assignedProposalId, "담당자가 아닌 사용자는 답변할 수 없습니다.");
        proposalService.editManagerComment(editDto);
    }

    /**
     * 5. 부서답변 상태에서도 의견을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("manager1@googl.co.kr")
    public void T_5_부서답변_상태에서도_의견을_수정할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        ProposalManagerCommentEditDto editDto = ProposalManagerCommentEditDto.of(completedProposalId, "완료된 담당자 답변을 수정할 수 있습니다.");
        Proposal proposal = proposalService.editManagerComment(editDto);

        ProposalDto proposalDto = proposalService.getProposal(equalId(proposal.getId()), projection);
        assertThat(proposalDto.getManagerCommentDate().format(dateTimeFormatter), is(now));
        assertThat(proposalDto.getManagerComment(), is(editDto.getComment()));
    }
}
