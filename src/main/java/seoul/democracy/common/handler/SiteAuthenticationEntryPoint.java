package seoul.democracy.common.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import seoul.democracy.common.config.ApiPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SiteAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final ApiPathRequestMatcher apiPathRequestMatcher;

    public SiteAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
        apiPathRequestMatcher = new ApiPathRequestMatcher();
    }


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        if (apiPathRequestMatcher.matches(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"msg\":\"로그인이 필요합니다.\"}");
        } else {
            super.commence(request, response, authException);
        }
    }
}
