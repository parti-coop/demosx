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
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.history.domain.IssueHistory;
import seoul.democracy.history.dto.IssueHistoryDto;
import seoul.democracy.history.service.IssueHistoryService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seoul.democracy.history.dto.IssueHistoryDto.projection;
import static seoul.democracy.history.predicate.IssueHistoryPredicate.equalId;


/**
 * epic : 9. 토론관리
 * story : 9.7 관리자는 토론 히스토리를 삭제할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_9_7_관리자는_토론_히스토리를_삭제할_수_있다 {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
    private final static String ip = "127.0.0.2";
    private MockHttpServletRequest request;

    @Autowired
    private IssueHistoryService historyService;

    private final Long historyId = 101L;
    private final Long deletedHistoryId = 102L;
    private final Long notExistsId = 999L;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setRemoteAddr(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    /**
     * 1. 관리자는 토론에 히스토리를 삭제할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_1_관리자는_토론에_히스토리를_삭제할_수_있다() {
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        IssueHistory history = historyService.delete(historyId);

        IssueHistoryDto historyDto = historyService.getHistory(equalId(history.getId()), projection);
        assertThat(historyDto.getModifiedDate().format(dateTimeFormatter), is(now));
        assertThat(historyDto.getModifiedBy().getEmail(), is("admin1@googl.co.kr"));
        assertThat(historyDto.getModifiedIp(), is(ip));

        assertThat(historyDto.getStatus(), is(IssueHistory.Status.DELETE));
    }

    /**
     * 2. 매니저는 히스토리를 삭제할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_2_매니저는_히스토리를_삭제할_수_없다() {
        historyService.delete(historyId);
    }

    /**
     * 3. 사용자는 히스토리를 삭제할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_3_사용자는_히스토리를_삭제할_수_없다() {
        historyService.delete(historyId);
    }

    /**
     * 4. 없는 히스토리는 삭제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_4_없는_히스토리는_삭제할_수_없다() {
        historyService.delete(notExistsId);
    }

    /**
     * 5. 삭제된 히스토리는 삭제할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_5_삭제된_히스토리는_삭제할_수_없다() {
        historyService.delete(deletedHistoryId);
    }
}
