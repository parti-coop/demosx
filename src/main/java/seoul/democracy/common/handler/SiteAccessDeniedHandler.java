package seoul.democracy.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import seoul.democracy.common.config.ApiPathRequestMatcher;
import seoul.democracy.user.utils.UserUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SiteAccessDeniedHandler implements AccessDeniedHandler {

    private final ApiPathRequestMatcher apiPathRequestMatcher;

    public SiteAccessDeniedHandler() {
        apiPathRequestMatcher = new ApiPathRequestMatcher();
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        if (apiPathRequestMatcher.matches(request)) {
            if (UserUtils.isLogin()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"msg\":\"해당 권한이 없습니다.\"}");
            } else {
                // login 필요
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"msg\":\"로그인이 필요합니다.\"}");
            }
        } else {
            if (UserUtils.isLogin()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "AccessDenied");
            } else if(request.getRequestURI().contains("logout.do")) {
                response.sendRedirect("/index.do");
            } else {
                // login 필요
                response.sendRedirect("/login.do");
            }
        }
    }
}
