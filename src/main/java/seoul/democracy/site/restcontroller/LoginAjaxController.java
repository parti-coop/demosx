package seoul.democracy.site.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.user.dto.UserPasswordResetDto;
import seoul.democracy.user.service.UserService;

import javax.validation.Valid;

@RestController
public class LoginAjaxController {

    private final UserService userService;

    @Autowired
    public LoginAjaxController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/ajax/reset-password", method = RequestMethod.POST)
    public ResultInfo resetPassword(@RequestBody @Valid UserPasswordResetDto resetDto) {
        userService.resetPassword(resetDto);

        return ResultInfo.of("패스워드를 설정하였습니다. 로그인해 주세요.");
    }
}
