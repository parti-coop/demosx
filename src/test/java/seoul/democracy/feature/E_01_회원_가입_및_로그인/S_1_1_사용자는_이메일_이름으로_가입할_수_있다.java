package seoul.democracy.feature.E_01_회원_가입_및_로그인;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.user.domain.Role;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.dto.UserCreateDto;
import seoul.democracy.user.dto.UserDto;
import seoul.democracy.user.service.UserService;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static seoul.democracy.user.dto.UserDto.projection;
import static seoul.democracy.user.predicate.UserPredicate.equalId;


/**
 * epic : 1. 회원 가입 및 로그인
 * story : 1.1 사용자는 이메일, 이름으로 가입할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_1_1_사용자는_이메일_이름으로_가입할_수_있다 {

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 1. 이름, 이메일, 연락처, 패스워드를 입력하여 가입할 수 있다.
     */
    @Test
    public void T_1_사용자는_이메일_이름으로_가입할_수_있다() {
        UserCreateDto createDto = UserCreateDto.of("user999@test.com", "유저999", "12345");
        User user = userService.create(createDto, "127.0.0.1");
        assertThat(user.getId(), is(notNullValue()));

        UserDto userDto = userService.getUser(equalId(user.getId()), projection);
        assertThat(userDto.getEmail(), is(createDto.getEmail()));
        assertThat(userDto.getName(), is(createDto.getName()));

        assertThat(userDto.getStatus(), is(User.Status.ACTIVATED));
        assertThat(userDto.getRole(), is(Role.ROLE_USER));
    }

    /**
     * 2. 이미 가입된 이메일로 가입할 수 없다.
     */
    @Test(expected = AlreadyExistsException.class)
    public void T_2_이미_가입된_이메일로_가입할_수_없다() {
        UserCreateDto createDto = UserCreateDto.of("user1@googl.co.kr", "유저999", "12345");
        userService.create(createDto, "127.0.0.1");
    }
}