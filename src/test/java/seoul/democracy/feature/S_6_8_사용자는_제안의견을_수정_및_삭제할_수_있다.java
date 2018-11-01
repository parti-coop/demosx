package seoul.democracy.feature;

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
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.domain.ProposalOpinion;
import seoul.democracy.opinion.dto.ProposalOpinionDto;
import seoul.democracy.opinion.dto.ProposalOpinionUpdateDto;
import seoul.democracy.proposal.service.ProposalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.opinion.dto.ProposalOpinionDto.projection;
import static seoul.democracy.opinion.predicate.ProposalOpinionPredicate.equalId;


/**
 * epic : 6. 제안
 * story : 6.8 사용자는 제안의견을 수정 및 삭제할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_6_8_사용자는_제안의견을_수정_및_삭제할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";


    private Long opinionId = 1L;
    private Long deletedOpinionId = 2L;
    private Long blockedOpinionId = 3L;
    private Long notExistsId = 999L;

    @Autowired
    private ProposalService proposalService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 사용자는 본인의견을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_01_사용자는_본인의견을_수정할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        ProposalOpinionUpdateDto updateDto = ProposalOpinionUpdateDto.of(opinionId, "제안의견 수정합니다.");
        ProposalOpinion opinion = proposalService.updateOpinion(updateDto, ip);

        ProposalOpinionDto opinionDto = proposalService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getContent(), is(updateDto.getContent()));
    }

    /**
     * 2. 사용자는 본인의견을 삭제할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_02_사용자는_본인의견을_삭제할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        Opinion opinion = proposalService.deleteOpinion(opinionId, ip);

        ProposalOpinionDto opinionDto = proposalService.getOpinion(equalId(opinion.getId()), projection);
        assertThat(opinionDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(opinionDto.getModifiedBy().getEmail(), is("user1@googl.co.kr"));
        assertThat(opinionDto.getModifiedIp(), is(ip));

        assertThat(opinionDto.getStatus(), is(Opinion.Status.DELETE));
    }

    /**
     * 3. 다른 사용자의 의견을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_03_다른_사용자의_의견을_수정할_수_없다() {
        ProposalOpinionUpdateDto updateDto = ProposalOpinionUpdateDto.of(opinionId, "다른 사용자가 제안의견을 수정합니다.");
        proposalService.updateOpinion(updateDto, ip);
    }

    /**
     * 4. 다른 사용자의 의견을 삭제할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user2@googl.co.kr")
    public void T_04_다른_사용자의_의견을_삭제할_수_없다() {
        proposalService.deleteOpinion(opinionId, ip);
    }

    /**
     * 5. 없는 의견을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_05_없는_의견을_수정할_수_없다() {
        ProposalOpinionUpdateDto updateDto = ProposalOpinionUpdateDto.of(notExistsId, "없는 제안의견 수정합니다.");
        proposalService.updateOpinion(updateDto, ip);
    }

    /**
     * 6. 없는 의견을 삭제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_06_없는_의견을_삭제할_수_없다() {
        proposalService.deleteOpinion(notExistsId, ip);
    }

    /**
     * 7. 삭제된 의견을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_07_삭제된_의견을_수정할_수_없다() {
        ProposalOpinionUpdateDto updateDto = ProposalOpinionUpdateDto.of(deletedOpinionId, "삭제된 의견은 수정할 수 없다.");
        proposalService.updateOpinion(updateDto, ip);
    }

    /**
     * 8. 삭제된 의견을 삭제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_08_삭제된_의견을_삭제할_수_없다() {
        proposalService.deleteOpinion(deletedOpinionId, ip);
    }

    /**
     * 9. 블럭된 의견을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_09_블럭된_의견을_수정할_수_없다() {
        ProposalOpinionUpdateDto updateDto = ProposalOpinionUpdateDto.of(blockedOpinionId, "블럭된 의견은 수정할 수 없다.");
        proposalService.updateOpinion(updateDto, ip);
    }

    /**
     * 10. 블럭된 의견을 삭제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_10_블럭된_의견을_삭제할_수_없다() {
        proposalService.deleteOpinion(blockedOpinionId, ip);
    }
}
