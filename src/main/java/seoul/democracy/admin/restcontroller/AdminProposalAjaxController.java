package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.service.ProposalService;

import static seoul.democracy.proposal.dto.ProposalDto.projectionForAdminList;
import static seoul.democracy.proposal.predicate.ProposalPredicate.containsTitleOrCreatedByNameAndEqualCategory;

@RestController
@RequestMapping("/admin/ajax/issue/proposals")
public class AdminProposalAjaxController {

    private final ProposalService proposalService;

    @Autowired
    public AdminProposalAjaxController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProposalDto> getProposals(@RequestParam(value = "search") String search,
                                          @RequestParam(value = "category", required = false) String category,
                                          @PageableDefault Pageable pageable) {
        return proposalService.getProposals(containsTitleOrCreatedByNameAndEqualCategory(search, category), pageable, projectionForAdminList);
    }
}
