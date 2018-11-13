package seoul.democracy.debate.service;

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
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateDto;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.debate.dto.DebateDto.projection;
import static seoul.democracy.debate.predicate.DebatePredicate.equalId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DebateService_IntegrationTest {

    @Autowired
    private DebateService debateService;

    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_진행예정에서_진행중으로_변경되는_토론테스트() {
        debateService.updateDebateProcess();

        DebateDto debateDto = debateService.getDebate(equalId(501L), projection, true, true);
        assertThat(debateDto.getProcess(), is(Debate.Process.PROGRESS));
    }

    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_진행예정에서_완료로_변경되는_토론테스트() {
        debateService.updateDebateProcess();

        DebateDto debateDto = debateService.getDebate(equalId(502L), projection, true, true);
        assertThat(debateDto.getProcess(), is(Debate.Process.COMPLETE));
    }

    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_3_진행중에서_진행예정으로_변경되는_토론테스트() {
        debateService.updateDebateProcess();

        DebateDto debateDto = debateService.getDebate(equalId(503L), projection, true, true);
        assertThat(debateDto.getProcess(), is(Debate.Process.INIT));
    }

    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_4_진행중에서_완료로_변경되는_토론테스트() {
        debateService.updateDebateProcess();

        DebateDto debateDto = debateService.getDebate(equalId(504L), projection, true, true);
        assertThat(debateDto.getProcess(), is(Debate.Process.COMPLETE));
    }

    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_5_완료에서_진행예정으로_변경되는_토론테스트() {
        debateService.updateDebateProcess();

        DebateDto debateDto = debateService.getDebate(equalId(505L), projection, true, true);
        assertThat(debateDto.getProcess(), is(Debate.Process.INIT));
    }

    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_6_완료에서_진행중으로_변경되는_토론테스트() {
        debateService.updateDebateProcess();

        DebateDto debateDto = debateService.getDebate(equalId(506L), projection, true, true);
        assertThat(debateDto.getProcess(), is(Debate.Process.PROGRESS));
    }
}
