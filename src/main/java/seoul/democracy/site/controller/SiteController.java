package seoul.democracy.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import seoul.democracy.issue.domain.Issue;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.service.ProposalService;

import static seoul.democracy.proposal.dto.ProposalDto.projectionForSiteList;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalStatus;

@Controller
public class SiteController {

    private final ProposalService proposalService;

    private final Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, "createdDate");

    @Autowired
    public SiteController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    /**
     * home 화면
     */
    @RequestMapping(value = "/index.do", method = RequestMethod.GET)
    public String index(Model model) {

        Page<ProposalDto> proposals = proposalService.getProposals(equalStatus(Issue.Status.OPEN), pageable, projectionForSiteList);
        model.addAttribute("page", proposals);

        return "/site/index";
    }

    /**
     * 오픈소스 스태틱 페이지
     */
    @RequestMapping(value = "/intro.do", method = RequestMethod.GET)
    public String intro() {
        return "/site/static/intro";
    }

    /**
     * 개인정보처리방침
     */
    @RequestMapping(value = "/privacy.do", method = RequestMethod.GET)
    public String privacy() {
        return "/site/static/privacy";
    }

    /**
     * 이용약관
     */
    @RequestMapping(value = "/terms.do", method = RequestMethod.GET)
    public String terms() {
        return "/site/static/terms";
    }
}
