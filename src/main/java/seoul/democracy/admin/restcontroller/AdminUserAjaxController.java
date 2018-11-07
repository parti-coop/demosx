package seoul.democracy.admin.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.user.domain.Role;
import seoul.democracy.user.dto.UserDto;
import seoul.democracy.user.service.UserService;

import static seoul.democracy.user.dto.UserDto.projectionForAdminList;
import static seoul.democracy.user.dto.UserDto.projectionForBasic;
import static seoul.democracy.user.predicate.UserPredicate.containsNameOrEmail;
import static seoul.democracy.user.predicate.UserPredicate.containsNameOrEmailAndRole;

@RestController
@RequestMapping("/admin/ajax/users")
public class AdminUserAjaxController {

    private final UserService userService;

    @Autowired
    public AdminUserAjaxController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<UserDto> getUsers(@RequestParam(value = "search") String search,
                                  @PageableDefault Pageable pageable) {

        return userService.getUsers(containsNameOrEmail(search), pageable, projectionForAdminList);
    }

    @RequestMapping(value = "/managers", method = RequestMethod.GET)
    public Page<UserDto> getManagers(@RequestParam(value = "search") String search,
                                     @PageableDefault Pageable pageable) {

        return userService.getUsers(containsNameOrEmailAndRole(search.trim(), Role.ROLE_MANAGER), pageable, projectionForBasic);
    }
}
