package seoul.democracy.common.config;

import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmartHttpSessionStrategy implements HttpSessionStrategy {

    final private HttpSessionStrategy browser;

    final private HttpSessionStrategy api;

    public SmartHttpSessionStrategy() {
        this.browser = new CookieHttpSessionStrategy();
        this.api = new HeaderHttpSessionStrategy();
    }

    @Override
    public String getRequestedSessionId(HttpServletRequest request) {
        return getStrategy(request).getRequestedSessionId(request);
    }

    @Override
    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        getStrategy(request).onNewSession(session, request, response);
    }

    @Override
    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
        getStrategy(request).onInvalidateSession(request, response);
    }

    private HttpSessionStrategy getStrategy(HttpServletRequest request) {
        return (request.getRequestURI().startsWith("/api/") || request.getRequestURI().startsWith("/admin/api/")) ? this.api : this.browser;
    }
}
