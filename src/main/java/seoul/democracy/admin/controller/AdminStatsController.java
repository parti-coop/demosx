package seoul.democracy.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/stats")
public class AdminStatsController {

    /**
     * 관리자 > 통계 > 게시물 통계
     */
    @RequestMapping(value = "/issue.do", method = RequestMethod.GET)
    public String issue() {
        return "/admin/stats/issue";
    }

    /**
     * 관리자 > 통계 > 댓글 통계
     */
    @RequestMapping(value = "/opinion.do", method = RequestMethod.GET)
    public String opinion() {
        return "/admin/stats/opinion";
    }
}
