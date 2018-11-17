package seoul.democracy.site.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
    "file:src/test/resources/egovframework/spring-test/context-*.xml",
    "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-*.xml"
})
public class LoginController_IntegrationTest {

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

    @Test
    public void login_success() throws Exception {
        mockMvc
            .perform(formLogin("/loginProcess.do")
                         .user("id", "admin1@googl.co.kr")
                         .password("pw", "12345"))
            .andDo(print())
            .andExpect(status().isFound())
            .andExpect(authenticated().withUsername("admin1@googl.co.kr"));
    }

    @Test
    public void wrongPassword_login_fail() throws Exception {
        mockMvc
            .perform(formLogin("/loginProcess.do")
                         .user("id", "user1")
                         .password("pw", "invalid"))
            .andDo(print())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/login.do?login_error=1"))
            .andExpect(unauthenticated());
    }

    @Test
    public void wrongId_login_fail() throws Exception {
        mockMvc
            .perform(formLogin("/loginProcess.do")
                         .user("id", "wrong_id")
                         .password("pw", "invalid"))
            .andDo(print())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/login.do?login_error=1"))
            .andExpect(unauthenticated());
    }
}