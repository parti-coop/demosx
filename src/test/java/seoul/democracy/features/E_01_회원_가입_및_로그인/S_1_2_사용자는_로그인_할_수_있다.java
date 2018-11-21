package seoul.democracy.features.E_01_회원_가입_및_로그인;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import seoul.democracy.user.dto.UserLoginDto;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static seoul.democracy.common.util.JsonUtils.asJsonString;


/**
 * epic : 1. 회원 가입 및 로그인
 * story : 1.2 사용자는 로그인 할 수 있다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
@Transactional
@Rollback
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S_1_2_사용자는_로그인_할_수_있다 {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders
                           .webAppContextSetup(this.wac)
                           .addFilters(springSecurityFilterChain)
                           .build();
    }


    /**
     * 1. 사용자는 로그인 할 수 있다.
     */
    @Test
    public void T_1_사용자는_로그인_할_수_있다() throws Exception {
        UserLoginDto loginDto = UserLoginDto.of("admin1@googl.co.kr", "12345");
        mockMvc.perform(post("/ajax/site/login")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(asJsonString(loginDto)).with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }

    /**
     * 2. 비밀번호가 틀린 경우 로그인할 수 없다.
     */
    @Test
    public void T_2_비밀번호가_틀린_경우_로그인할_수_없다() throws Exception {
        UserLoginDto loginDto = UserLoginDto.of("admin1@googl.co.kr", "wrong_password");
        mockMvc.perform(post("/ajax/site/login")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(asJsonString(loginDto)).with(csrf()))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    /**
     * 3. 등록되지 않은 이메일은 로그인할 수 없다.
     */
    @Test
    public void T_3_등록되지_않은_이메일은_로그인할_수_없다() throws Exception {
        UserLoginDto loginDto = UserLoginDto.of("notexists_email@googl.co.kr", "12345");
        mockMvc.perform(post("/ajax/site/login")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(asJsonString(loginDto)).with(csrf()))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();
    }

}