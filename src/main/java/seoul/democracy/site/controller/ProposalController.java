package seoul.democracy.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.service.CategoryService;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.predicate.ProposalPredicate;
import seoul.democracy.proposal.service.ProposalService;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static seoul.democracy.issue.domain.Issue.Status.OPEN;
import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;
import static seoul.democracy.proposal.dto.ProposalDto.projectionForAdminDetail;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalIdAndStatus;

@Controller
public class ProposalController {

    private final ProposalService proposalService;
    private final CategoryService categoryService;

    @Autowired
    public ProposalController(ProposalService proposalService,
                              CategoryService categoryService) {
        this.proposalService = proposalService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/proposal-list.do", method = RequestMethod.GET)
    public String proposalList(@RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "search", required = false) String search,
                               @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
                               Model model) {

        Page<ProposalDto> proposals = proposalService.getProposals(ProposalPredicate.predicateForSiteList(search, category), pageable, ProposalDto.projectionForAdminList);
        model.addAttribute("page", proposals);

        List<CategoryDto> categories = categoryService.getCategories(enabled(), projectionForFilter);
        model.addAttribute("categories", categories);

        return "/site/proposal/list";
    }

    @RequestMapping(value = "/proposal.do", method = RequestMethod.GET)
    public String proposal(@RequestParam("id") Long id,
                           Model model) {
        ProposalDto proposalDto = proposalService.getProposal(equalIdAndStatus(id, OPEN), projectionForAdminDetail);

        model.addAttribute("proposal", proposalDto);

        return "/site/proposal/detail";
    }
}
