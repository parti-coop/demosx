package seoul.democracy.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {


    @RequestMapping(value = "/index.do", method= RequestMethod.GET)
    public String index() {
        return "/admin/index";
    }
}
