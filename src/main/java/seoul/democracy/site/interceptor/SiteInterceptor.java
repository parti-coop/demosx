package seoul.democracy.site.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import seoul.democracy.user.utils.UserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SiteInterceptor extends HandlerInterceptorAdapter {

    public SiteInterceptor() {
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

        if (modelAndView == null || modelAndView.getViewName().startsWith("redirect:")) return;

        modelAndView.addObject("loginUser", UserUtils.getLoginUser());
    }
}
