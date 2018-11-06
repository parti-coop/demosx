package seoul.democracy.user.repository;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.user.domain.Role;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static seoul.democracy.user.dto.UserDto.projection;
import static seoul.democracy.user.dto.UserDto.projectionForAdminList;
import static seoul.democracy.user.predicate.UserPredicate.equalId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepository_IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void projection_동작확인() {
        UserDto userDto = userRepository.findOne(equalId(11L), projection);

        assertThat(userDto.getId(), is(11L));
        assertThat(userDto.getCreatedDate(), is(LocalDateTime.of(2018, 10, 11, 10, 0)));
        assertThat(userDto.getEmail(), is("manager1@googl.co.kr"));
        assertThat(userDto.getRole(), is(Role.ROLE_MANAGER));
        assertThat(userDto.getStatus(), is(User.Status.ACTIVATED));
        assertThat(userDto.getName(), is("매니저1"));
        assertThat(userDto.getLoginDate(), is(LocalDateTime.of(2018, 11, 11, 5, 0)));
        assertThat(userDto.getPhoto(), is("image11.jpg"));
        assertThat(userDto.getDepartment1(), is("부서1-1"));
        assertThat(userDto.getDepartment2(), is("부서1-2"));
        assertThat(userDto.getDepartment3(), is("부서1-3"));
    }

    @Test
    public void projectionForAdminList_동작확인() {
        UserDto userDto = userRepository.findOne(equalId(11L), projectionForAdminList);

        assertThat(userDto.getId(), is(11L));
        assertThat(userDto.getCreatedDate(), is(LocalDateTime.of(2018, 10, 11, 10, 0)));
        assertThat(userDto.getEmail(), is("manager1@googl.co.kr"));
        assertThat(userDto.getRole(), is(Role.ROLE_MANAGER));
        assertThat(userDto.getStatus(), is(User.Status.ACTIVATED));
        assertThat(userDto.getName(), is("매니저1"));
        assertThat(userDto.getLoginDate(), is(LocalDateTime.of(2018, 11, 11, 5, 0)));
        assertThat(userDto.getPhoto(), is(nullValue()));
        assertThat(userDto.getDepartment1(), is(nullValue()));
        assertThat(userDto.getDepartment2(), is(nullValue()));
        assertThat(userDto.getDepartment3(), is(nullValue()));
    }

}