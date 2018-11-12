package seoul.democracy.admin.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.user.domain.Role;
import seoul.democracy.user.dto.UserDto;
import seoul.democracy.user.dto.UserManagerCreateDto;
import seoul.democracy.user.dto.UserManagerUpdateDto;
import seoul.democracy.user.service.UserService;

import javax.validation.Valid;

import static seoul.democracy.user.dto.UserDto.*;
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

    @RequestMapping(value = "/role-user", method = RequestMethod.GET)
    public Page<UserDto> getUsersWithRoleUser(@RequestParam(value = "search", required = false) String search,
                                     @PageableDefault Pageable pageable) {

        return userService.getUsers(containsNameOrEmailAndRole(search, Role.ROLE_USER), pageable, projectionForBasic);
    }

    @RequestMapping(value = "/role-manager", method = RequestMethod.GET)
    public Page<UserDto> getUsersWithRoleManager(@RequestParam(value = "search", required = false) String search,
                                     @PageableDefault Pageable pageable) {

        return userService.getUsers(containsNameOrEmailAndRole(search, Role.ROLE_MANAGER), pageable, projectionForAdminManager);
    }

    @RequestMapping(value = "/managers", method = RequestMethod.POST)
    public ResultInfo createManger(@RequestBody @Valid UserManagerCreateDto createDto) {
        userService.createManager(createDto);

        return ResultInfo.of("담당자를 지정하였습니다.");
    }

    @RequestMapping(value = "/managers/{id}", method = RequestMethod.PUT)
    public ResultInfo createManger(@PathVariable("id") Long id,
                                   @RequestBody @Valid UserManagerUpdateDto updateDto) {
        userService.updateManager(updateDto);

        return ResultInfo.of("담당자 정보를 수정하였습니다.");
    }

    @RequestMapping(value = "/managers/{id}", method = RequestMethod.DELETE)
    public ResultInfo createManger(@PathVariable("id") Long id) {
        userService.deleteManager(id);

        return ResultInfo.of("담당자 지정을 해제하였습니다.");
    }
}
