package seoul.democracy.site.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.dto.UserPasswordChangeDto;
import seoul.democracy.user.dto.UserUpdateDto;
import seoul.democracy.user.service.UserService;
import seoul.democracy.user.utils.UserUtils;

import javax.validation.Valid;

@RestController
@RequestMapping("/ajax/mypage")
public class MypageAjaxController {

    private final UserService userService;

    @Autowired
    public MypageAjaxController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    public ResultInfo updateUser(@RequestBody @Valid UserUpdateDto updateDto) {
        User user = userService.update(updateDto);
        UserUtils.updateLoginUser(user);

        return ResultInfo.of("내 정보를 수정하였습니다.");
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.PUT)
    public ResultInfo changePassword(@RequestBody @Valid UserPasswordChangeDto changeDto) {
        userService.changePassword(changeDto);

        return ResultInfo.of("패스워드를 수정하였습니다.");
    }
}
