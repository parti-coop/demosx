package seoul.democracy.site.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.proposal.dto.ProposalCreateDto;
import seoul.democracy.proposal.dto.ProposalUpdateDto;
import seoul.democracy.proposal.service.ProposalService;

import javax.validation.Valid;

@RestController
@RequestMapping("/ajax/mypage")
public class ProposalAjaxController {

    private final ProposalService proposalService;

    @Autowired
    public ProposalAjaxController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @RequestMapping(value = "/proposals", method = RequestMethod.POST)
    public ResultInfo newProposal(@RequestBody @Valid ProposalCreateDto createDto) {
        proposalService.create(createDto);

        return ResultInfo.of("제안을 등록하였습니다.");
    }

    @RequestMapping(value = "/proposals/{id}", method = RequestMethod.PUT)
    public ResultInfo editProposal(@PathVariable("id") Long id,
                                   @RequestBody @Valid ProposalUpdateDto updateDto) {
        proposalService.update(updateDto);

        return ResultInfo.of("제안을 수정하였습니다.");
    }

    @RequestMapping(value = "/proposals/{id}", method = RequestMethod.DELETE)
    public ResultInfo deleteProposal(@PathVariable("id") Long id) {
        proposalService.delete(id);

        return ResultInfo.of("제안을 삭제하였습니다.");
    }
}
