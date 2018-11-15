package seoul.democracy.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.user.dto.UserCreateDto;
import seoul.democracy.user.service.UserService;
import seoul.democracy.user.utils.UserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 로그인 화면
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public String login(@RequestParam(value = "login_error", required = false) Long loginError,
                        HttpServletRequest request,
                        HttpSession session,
                        Model model) {

        model.addAttribute("loginError", loginError);

        if (loginError == null) {
            String target = request.getHeader("Referer");
            if (StringUtils.hasText(target)) {
                session.setAttribute("SITE_LOGIN_REDIRECT_URL", target);
            }
        }

        return UserUtils.isLogin() ? "redirect:/index.do" : "/site/login";
    }

    /**
     * 회원가입 화면
     */
    @RequestMapping(value = "/join.do", method = RequestMethod.GET)
    public String join(@ModelAttribute("createDto") UserCreateDto createDto) {
        return UserUtils.isLogin() ? "redirect:/index.do" : "/site/join";
    }

    @RequestMapping(value = "/join.do", method = RequestMethod.POST)
    public String join(@ModelAttribute("createDto") @Valid UserCreateDto createDto,
                       BindingResult result,
                       Model model) {
        if (UserUtils.isLogin()) return "redirect:/index.do";
        if (result.hasErrors()) return "/site/join";

        try {
            userService.create(createDto);
        } catch (AlreadyExistsException e) {
            result.rejectValue("email", "email.error", e.getLocalizedMessage());
            return "/site/join";
        }

        model.addAttribute("joinSuccess", true);
        return "/site/join";
    }

    /**
     * 비밀번호 찾기
     */
    @RequestMapping(value = "/find-password.do", method = RequestMethod.GET)
    public String findPassword() {
        return UserUtils.isLogin() ? "redirect:/index.do" : "/site/find-password";
    }
}
