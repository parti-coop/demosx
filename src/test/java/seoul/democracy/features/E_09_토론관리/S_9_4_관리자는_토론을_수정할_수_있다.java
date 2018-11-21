package seoul.democracy.features.E_09_토론관리;

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
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.dto.DebateUpdateDto;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.opinion.domain.OpinionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static seoul.democracy.debate.dto.DebateDto.projection;
import static seoul.democracy.debate.predicate.DebatePredicate.equalId;


/**
 * epic : 9. 토론관리
 * story : 9.4 관리자는 토론을 수정할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_9_4_관리자는_토론을_수정할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private DebateService debateService;

    private Long debateId = 101L;
    private Long notExistsId = 999L;

    private DebateUpdateDto updateDto;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        updateDto = DebateUpdateDto.of(debateId, "thumbnail.jpg", "복지", OpinionType.PROPOSAL,
            LocalDate.of(2019, 10, 10), LocalDate.of(2019, 12, 12),
            "토론 + 제안의견", "토론 한줄 설명", "제안의견인 토론입니다.", Issue.Status.OPEN,
            Arrays.asList(IssueFileDto.of("파일1", "file1"), IssueFileDto.of("파일2", "file2")),
            Arrays.asList(1L, 11L), null);
    }

    /**
     * 1. 관리자는 토론을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("admin2@googl.co.kr")
    public void T_1_관리자는_토론을_수정할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);

        Debate debate = debateService.update(updateDto);

        DebateDto debateDto = debateService.getDebate(equalId(debate.getId()), projection, true, true);
        assertThat(debateDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(debateDto.getModifiedBy().getEmail(), is("admin2@googl.co.kr"));
        assertThat(debateDto.getModifiedIp(), is(ip));

        assertThat(debateDto.getOpinionType(), is(OpinionType.PROPOSAL));
        assertThat(debateDto.getCategory().getName(), is(updateDto.getCategory()));

        assertThat(debateDto.getThumbnail(), is(updateDto.getThumbnail()));
        assertThat(debateDto.getStartDate(), is(updateDto.getStartDate()));
        assertThat(debateDto.getEndDate(), is(updateDto.getEndDate()));

        assertThat(debateDto.getTitle(), is(updateDto.getTitle()));
        assertThat(debateDto.getExcerpt(), is(updateDto.getExcerpt()));
        assertThat(debateDto.getContent(), is(updateDto.getContent()));

        assertThat(debateDto.getStatus(), is(updateDto.getStatus()));

        assertThat(debateDto.getFiles(), contains(updateDto.getFiles().toArray(new IssueFileDto[0])));
        assertThat(debateDto.getRelations(), contains(updateDto.getRelations().toArray(new Long[0])));
    }

    /**
     * 2. 없는 토론을 수정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_2_없는_토론을_수정할_수_없다() {
        updateDto.setId(notExistsId);
        debateService.update(updateDto);
    }

    /**
     * 3. 매니저는 토론을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_3_매니저는_토론을_수정할_수_없다() {
        debateService.update(updateDto);
    }

    /**
     * 4. 사용자는 토론을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_사용자는_토론을_수정할_수_없다() {
        debateService.update(updateDto);
    }

    /**
     * 5. 존재하지 않는 항목을 연관으로 수정할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_5_존재하지_않는_항목을_연관으로_수정할_수_없다() {
        Long notExistsRelation = 999L;
        updateDto.setRelations(Arrays.asList(1L, 11L, notExistsRelation));
        debateService.update(updateDto);
    }

    /**
     * 6. 토론기간이 오늘이 포함될 경우 진행중 상태가 된다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_6_토론기간이_오늘이_포함될_경우_진행중_상태가_된다() {
        LocalDate now = LocalDate.now();
        updateDto.setStartDate(now.minusDays(1));
        updateDto.setEndDate(now.plusDays(1));
        Debate debate = debateService.update(updateDto);
        assertThat(debate.getId(), is(notNullValue()));

        DebateDto debateDto = debateService.getDebate(equalId(debate.getId()), projection, true, true);
        assertThat(debateDto.getProcess(), is(Debate.Process.PROGRESS));
    }

    /**
     * 7. 토론기간이 지난 경우 종료 상태가 된다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_7_토론기간이_지난_경우_종료_상태가_된다() {
        LocalDate now = LocalDate.now();
        updateDto.setStartDate(now.minusDays(2));
        updateDto.setEndDate(now.minusDays(1));
        Debate debate = debateService.update(updateDto);
        assertThat(debate.getId(), is(notNullValue()));

        DebateDto debateDto = debateService.getDebate(equalId(debate.getId()), projection, true, true);
        assertThat(debateDto.getProcess(), is(Debate.Process.COMPLETE));
    }

    /**
     * 8. 토론기간이 이전인 경우 진행예정 상태가 된다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_8_토론기간이_이전인_경우_진행예정_상태가_된다() {
        LocalDate now = LocalDate.now();
        updateDto.setStartDate(now.plusDays(1));
        updateDto.setEndDate(now.plusDays(2));
        Debate debate = debateService.update(updateDto);
        assertThat(debate.getId(), is(notNullValue()));

        DebateDto debateDto = debateService.getDebate(equalId(debate.getId()), projection, true, true);
        assertThat(debateDto.getProcess(), is(Debate.Process.INIT));
    }
}
