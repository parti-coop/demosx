package seoul.democracy.features.E_01_회원_가입_및_로그인;

import org.junit.Before;
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
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.dto.UserDto;
import seoul.democracy.user.dto.UserPasswordChangeDto;
import seoul.democracy.user.predicate.UserPredicate;
import seoul.democracy.user.service.UserService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * epic : 1. 회원 가입 및 로그인
 * story : 1.4 사용자는 비밀번호를 변경할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_1_4_사용자는_비밀번호를_변경할_수_있다 {

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 이름, 프로필 사진을 수정할 수 있다.
     */
    @Test
    @WithUserDetails("user1@googl.co.kr")
    public void T_1_사용자는_비밀번호를_변경할_수_있다() {
        UserPasswordChangeDto changeDto = UserPasswordChangeDto.of("12345", "09876");
        User user = userService.changePassword(changeDto);
        assertThat(user.getId(), is(21L));

        userService.getUser(UserPredicate.equalEmail("user1@googl.co.kr"), UserDto.projection);

        assertThat(userService.matchPassword(changeDto.getChangePassword()), is(true));
    }

    /**
     * 2. 비밀번호 다를 경우 변경할 수 없다.
     */
    @Test(expected = BadRequestException.class)
    @WithUserDetails("user1@googl.co.kr")
    public void T_2_비밀번호_다를_경우_변경할_수_없다() {
        UserPasswordChangeDto changeDto = UserPasswordChangeDto.of("123", "09876");
        userService.changePassword(changeDto);
    }

}