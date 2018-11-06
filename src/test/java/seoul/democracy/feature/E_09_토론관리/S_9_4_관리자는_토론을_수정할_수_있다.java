package seoul.democracy.feature.E_09_토론관리;

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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
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
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_9_4_관리자는_토론을_수정할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";

    @Autowired
    private DebateService debateService;

    private Long debateId = 101L;
    private Long notExistsId = 999L;

    private DebateUpdateDto updateDto;

    @Before
    public void setUp() throws Exception {
        updateDto = DebateUpdateDto.of(debateId, "thumbnail.jpg", "복지",
            LocalDate.of(2019, 10, 10), LocalDate.of(2019, 12, 12),
            "토론 + 제안의견", "제안의견인 토론입니다.", Issue.Status.OPEN,
            Arrays.asList(IssueFileDto.of("파일1", "file1"), IssueFileDto.of("파일2", "file2")),
            Arrays.asList(1L, 11L));
    }

    /**
     * 1. 관리자는 토론을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_토론을_수정할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);

        Debate debate = debateService.update(updateDto, ip);

        DebateDto debateDto = debateService.getDebate(equalId(debate.getId()), projection, true, true);
        assertThat(debateDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(debateDto.getModifiedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(debateDto.getModifiedIp(), is(ip));

        assertThat(debateDto.getOpinionType(), is(OpinionType.PROPOSAL));
        assertThat(debateDto.getCategory().getName(), is(updateDto.getCategory()));

        assertThat(debateDto.getThumbnail(), is(updateDto.getThumbnail()));
        assertThat(debateDto.getStartDate(), is(updateDto.getStartDate()));
        assertThat(debateDto.getEndDate(), is(updateDto.getEndDate()));

        assertThat(debateDto.getTitle(), is(updateDto.getTitle()));
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
        debateService.update(updateDto, ip);
    }

    /**
     * 3. 매니저는 토론을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_3_매니저는_토론을_수정할_수_없다() {
        debateService.update(updateDto, ip);
    }

    /**
     * 4. 사용자는 토론을 수정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_4_사용자는_토론을_수정할_수_없다() {
        debateService.update(updateDto, ip);
    }
}
