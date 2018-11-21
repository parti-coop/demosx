package seoul.democracy.features.E_02_회원관리;

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
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.user.domain.Role;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.dto.UserDto;
import seoul.democracy.user.dto.UserManagerCreateDto;
import seoul.democracy.user.dto.UserManagerUpdateDto;
import seoul.democracy.user.predicate.UserPredicate;
import seoul.democracy.user.service.UserService;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * epic : 2. 회원관리
 * story : 2.3 관리자는 담당자를 지정할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_2_3_관리자는_담당자를_지정할_수_있다 {

    @Autowired
    private UserService userService;

    private UserManagerCreateDto createDto;
    private UserManagerUpdateDto updateDto;

    private final Long adminId = 2L;
    private final Long managerId = 11L;
    private final Long userId = 21L;

    @Before
    public void setUp() throws Exception {
        createDto = UserManagerCreateDto.of(userId, "여성", "부서1", "부서2", "부서3");
        updateDto = UserManagerUpdateDto.of(managerId, "여성", "부서1", "부서2", "부서3");
    }

    /**
     * 1. 관리자는 사용자를 담당자로 지정할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_01_관리자는_사용자를_담당자로_지정할_수_있다() {
        User user = userService.createManager(createDto);

        UserDto userDto = userService.getUser(UserPredicate.equalId(user.getId()), UserDto.projection);
        assertThat(userDto.getId(), is(createDto.getUserId()));
        assertThat(userDto.getRole(), is(Role.ROLE_MANAGER));
        assertThat(userDto.getCategory().getName(), is(createDto.getCategory()));
        assertThat(userDto.getDepartment1(), is(createDto.getDepartment1()));
        assertThat(userDto.getDepartment2(), is(createDto.getDepartment2()));
        assertThat(userDto.getDepartment3(), is(createDto.getDepartment3()));
    }

    /**
     * 2. 관리자는 담당자 부서정보를 변경할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_02_관리자는_담당자_부서정보를_변경할_수_있다() {
        User user = userService.updateManager(updateDto);

        UserDto userDto = userService.getUser(UserPredicate.equalId(user.getId()), UserDto.projection);
        assertThat(userDto.getId(), is(updateDto.getUserId()));
        assertThat(userDto.getRole(), is(Role.ROLE_MANAGER));
        assertThat(userDto.getCategory().getName(), is(updateDto.getCategory()));
        assertThat(userDto.getDepartment1(), is(updateDto.getDepartment1()));
        assertThat(userDto.getDepartment2(), is(updateDto.getDepartment2()));
        assertThat(userDto.getDepartment3(), is(updateDto.getDepartment3()));
    }

    /**
     * 3. 관리자는 담당자 지정을 취소할 수 있다.
     */
    @Test
    @WithUserDetails("admin1@googl.co.kr")
    public void T_03_관리자는_담당자_지정을_취소할_수_있다() {
        User user = userService.deleteManager(managerId);

        UserDto userDto = userService.getUser(UserPredicate.equalId(user.getId()), UserDto.projection);
        assertThat(userDto.getId(), is(managerId));
        assertThat(userDto.getRole(), is(Role.ROLE_USER));
        assertThat(userDto.getCategory().getName(), is(nullValue()));
        assertThat(userDto.getDepartment1(), is(nullValue()));
        assertThat(userDto.getDepartment2(), is(nullValue()));
        assertThat(userDto.getDepartment3(), is(nullValue()));
    }

    /**
     * 4. 관리자를 담당자로 지정할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_04_관리자를_담당자로_지정할_수_없다() {
        createDto.setUserId(adminId);
        userService.createManager(createDto);
    }

    /**
     * 5. 담당자를 담당자로 지정할 수 없다.
     */
    @Test(expected = AlreadyExistsException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_05_담당자를_담당자로_지정할_수_없다() {
        createDto.setUserId(managerId);
        userService.createManager(createDto);
    }

    /**
     * 6. 관라지의 담당자 정보를 변경할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_06_관리자의_담당자_정보를_변경할_수_없다() {
        updateDto.setUserId(adminId);
        userService.updateManager(updateDto);
    }

    /**
     * 7. 사용자의 담당자 정보를 변경할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_07_사용자의_담당자_정보를_변경할_수_없다() {
        updateDto.setUserId(userId);
        userService.updateManager(updateDto);
    }

    /**
     * 8. 관리자를 담당자 지정 취소할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_08_관리자를_담당자_지정_취소할_수_없다() {
        userService.deleteManager(adminId);
    }

    /**
     * 9. 사용자를 담당자 지정 취소할 수 없다.
     */
    @Test(expected = NotFoundException.class)
    @WithUserDetails("admin1@googl.co.kr")
    public void T_09_사용자를_담당자_지정_취소할_수_없다() {
        userService.deleteManager(userId);
    }

    /**
     * 10. 매니저는 담당자를 지정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_10_매니저는_담당자를_지정할_수_없다() {
        userService.createManager(createDto);
    }

    /**
     * 11. 사용자는 담당자를 지정할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_11_사용자는_담당자를_지정할_수_없다() {
        userService.createManager(createDto);
    }

    /**
     * 12. 매니저는 담당자 정보를 변경할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_12_매니저는_담당자_정보를_변경할_수_없다() {
        userService.updateManager(updateDto);
    }

    /**
     * 13. 사용자는 담당자 정보를 변경할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_13_사용자는_담당자_정보를_변경할_수_없다() {
        userService.updateManager(updateDto);
    }

    /**
     * 14. 매니저는 담당자 지정 취소할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("manager1@googl.co.kr")
    public void T_14_매니저는_담당자_지정_취소할_수_없다() {
        userService.deleteManager(managerId);
    }

    /**
     * 15. 사용자는 담당자 지정 취소할 수 없다.
     */
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_15_사용자는_담당자_지정_취소할_수_없다() {
        userService.deleteManager(managerId);
    }
}
