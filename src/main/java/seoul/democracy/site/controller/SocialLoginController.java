package seoul.democracy.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.social.service.SocialService;
import seoul.democracy.user.utils.UserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
public class SocialLoginController {

    private final SocialService socialService;

    @Autowired
    public SocialLoginController(SocialService socialService) {
        this.socialService = socialService;
    }

    @RequestMapping(value = "/social-login.do", method = RequestMethod.GET)
    public String login(@RequestParam("provider") String provider,
                        HttpServletRequest request,
                        HttpSession session) throws IOException, ExecutionException, InterruptedException {
        if (UserUtils.isLogin()) return "redirect:/index.do";

        String target = request.getHeader("Referer");
        if (StringUtils.hasText(target)) {
            session.setAttribute("SITE_LOGIN_REDIRECT_URL", target);
        }

        if ("naver".equals(provider)) {
            return "redirect:" + socialService.naverAuthorizationUrl(session);
        } else if ("kakao".equals(provider)) {
            return "redirect:" + socialService.kakaoAuthorizationUrl(session);
        } else if ("twitter".equals(provider)) {
            return "redirect:" + socialService.twitterAuthorizationUrl(session);
        } else if ("facebook".equals(provider)) {
            return "redirect:" + socialService.facebookAuthorizationUrl(session);
        }

        return "redirect:/login.do";
    }
}
