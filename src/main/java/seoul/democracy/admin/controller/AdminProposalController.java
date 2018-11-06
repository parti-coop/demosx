package seoul.democracy.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/issue")
public class AdminProposalController {

    /**
     * 관리자 > 시민제안 > 제안관리
     */
    @RequestMapping(value = "/proposal.do", method = RequestMethod.GET)
    public String proposal() {
        return "/admin/proposal/list";
    }
}
