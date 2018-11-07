package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.proposal.domain.Proposal;
import seoul.democracy.proposal.dto.*;
import seoul.democracy.proposal.service.ProposalService;

import javax.validation.Valid;
import java.net.InetAddress;

import static seoul.democracy.proposal.dto.ProposalDto.projectionForAdminList;
import static seoul.democracy.proposal.dto.ProposalDto.projectionForAssignManager;
import static seoul.democracy.proposal.predicate.ProposalPredicate.containsTitleOrCreatedByNameAndEqualCategory;
import static seoul.democracy.proposal.predicate.ProposalPredicate.equalId;

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

    @RequestMapping(value = "/{proposalId}/category", method = RequestMethod.PATCH)
    public ResultInfo updateCategory(@PathVariable("proposalId") Long proposalId,
                                     @RequestBody @Valid ProposalCategoryUpdateDto updateDto,
                                     InetAddress address) {

        proposalService.updateCategory(updateDto, address.getHostName());

        return ResultInfo.of("카테고리를 업데이트 하였습니다.");
    }

    @RequestMapping(value = "/{proposalId}/closed", method = RequestMethod.PATCH)
    public ResultInfo closedProposal(@PathVariable("proposalId") Long proposalId,
                                     InetAddress address) {
        proposalService.block(proposalId, address.getHostName());

        return ResultInfo.of("비공개 상태입니다.");
    }

    @RequestMapping(value = "/{proposalId}/open", method = RequestMethod.PATCH)
    public ResultInfo openProposal(@PathVariable("proposalId") Long proposalId,
                                   InetAddress address) {
        proposalService.open(proposalId, address.getHostName());

        return ResultInfo.of("공개 상태입니다.");
    }

    @RequestMapping(value = "/{proposalId}/adminComment", method = RequestMethod.PATCH)
    public ResultInfo adminComment(@PathVariable("proposalId") Long proposalId,
                                   @RequestBody @Valid ProposalAdminCommentEditDto editDto) {

        proposalService.editAdminComment(editDto);

        return ResultInfo.of("관리자 댓글을 수정하였습니다.");
    }

    @RequestMapping(value = "/{proposalId}/assignManager", method = RequestMethod.PATCH)
    public ProposalDto assignManager(@PathVariable("proposalId") Long proposalId,
                                     @RequestBody @Valid ProposalManagerAssignDto assignDto) {

        Proposal proposal = proposalService.assignManager(assignDto);

        return proposalService.getProposal(equalId(proposal.getId()), projectionForAssignManager);
    }

    @RequestMapping(value = "/{proposalId}/managerComment", method = RequestMethod.PATCH)
    public ResultInfo managerComment(@PathVariable("proposalId") Long proposalId,
                                     @RequestBody @Valid ProposalManagerCommentEditDto editDto) {

        proposalService.editManagerComment(editDto);

        return ResultInfo.of("담당자 답변을 수정하였습니다.");
    }
}
