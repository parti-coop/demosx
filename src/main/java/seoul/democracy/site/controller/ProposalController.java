package seoul.democracy.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.service.ProposalService;

import static seoul.democracy.issue.domain.Issue.Status.OPEN;
import static seoul.democracy.proposal.dto.ProposalDto.projectionForAdminDetail;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalIdAndStatus;

@Controller
public class ProposalController {

    private final ProposalService proposalService;

    @Autowired
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @RequestMapping(value = "/proposal.do", method = RequestMethod.GET)
    public String proposal(@RequestParam("id") Long id,
                           Model model) {
        ProposalDto proposalDto = proposalService.getProposal(equalIdAndStatus(id, OPEN), projectionForAdminDetail);

        model.addAttribute("proposal", proposalDto);

        return "/site/proposal/detail";
    }
}
