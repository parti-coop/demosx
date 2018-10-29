package seoul.democracy.site.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.user.dto.UserLoginDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/ajax/site")
public class SiteAjaxController {

    final private AuthenticationManager authenticationManager;

    @Autowired
    public SiteAjaxController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResultInfo login(@RequestBody @Valid UserLoginDto loginDto, BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            throw new BadRequestException("login", "error.login", "아이디/패스워드를 확인해 주세요.");
        }

        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPw());
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        } catch (Exception e) {
            throw new BadRequestException("login", "error.login", "아이디/패스워드를 확인해 주세요.");
        }

        return ResultInfo.of(session.getId());
    }
}
