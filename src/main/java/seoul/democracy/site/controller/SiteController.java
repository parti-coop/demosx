package seoul.democracy.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SiteController {

    public SiteController() {
    }

    /**
     * home 화면
     */
    @RequestMapping(value = "/index.do", method = RequestMethod.GET)
    public String index() throws Exception {

        return "/site/index";
    }
}
