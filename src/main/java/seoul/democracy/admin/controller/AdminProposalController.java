package seoul.democracy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.service.CategoryService;
import seoul.democracy.proposal.dto.ProposalDto;
import seoul.democracy.proposal.service.ProposalService;

import java.util.List;

import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalId;

@Controller
@RequestMapping("/admin/issue")
public class AdminProposalController {

    private final CategoryService categoryService;
    private final ProposalService proposalService;

    @Autowired
    public AdminProposalController(CategoryService categoryService,
                                   ProposalService proposalService) {
        this.categoryService = categoryService;
        this.proposalService = proposalService;
    }

    @ModelAttribute("categories")
    public List<CategoryDto> getCategories() {
        return categoryService.getCategories(enabled(), projectionForFilter);
    }

    /**
     * 관리자 > 시민제안 > 제안관리
     */
    @RequestMapping(value = "/proposal.do", method = RequestMethod.GET)
    public String proposal() {
        return "/admin/proposal/list";
    }

    /**
     * 관리자 > 시민제안 > 제안관리 > 상세
     */
    @RequestMapping(value = "/proposal-detail.do", method = RequestMethod.GET)
    public String proposalDetail(@RequestParam("id") Long id,
                                 Model model) {

        ProposalDto proposalDto = proposalService.getProposal(equalId(id), ProposalDto.projectionForAdminDetail);
        model.addAttribute("proposal", proposalDto);

        return "/admin/proposal/detail";
    }
}
