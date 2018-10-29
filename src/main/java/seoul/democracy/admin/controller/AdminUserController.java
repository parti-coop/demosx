package seoul.democracy.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String list() {
        return "/admin/user/list";
    }
}
