package seoul.democracy.common.handler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import seoul.democracy.common.util.IpUtils;
import seoul.democracy.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SiteAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private final UserService userService;

    @Autowired
    public SiteAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {

        // login ip, 시간 저장
        String ip = IpUtils.getIp(request);
        userService.login(ip);

        // redirect url 확인
        String target = getSavedRequestTarget(request, response);
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (target == null) {
                target = (String) session.getAttribute("SITE_LOGIN_REDIRECT_URL");
            }
            session.removeAttribute("SITE_LOGIN_REDIRECT_URL");
        }

        clearAuthenticationAttributes(request);

        // ajax login
        if (request.getParameter("ajax") != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"msg\":\"로그인하였습니다.\"}");
            return;
        }

        if (StringUtils.hasText(target))
            getRedirectStrategy().sendRedirect(request, response, target);
        else getRedirectStrategy().sendRedirect(request, response, getDefaultTargetUrl());
    }

    private String getSavedRequestTarget(HttpServletRequest request,
                                         HttpServletResponse response) {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) return null;

        String targetUrl = savedRequest.getRedirectUrl();

        requestCache.removeRequest(request, response);

        if (StringUtils.hasText(targetUrl)) return targetUrl;

        return null;
    }

}
