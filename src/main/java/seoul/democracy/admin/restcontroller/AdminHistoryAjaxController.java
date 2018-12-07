package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.history.dto.IssueHistoryCreateDto;
import seoul.democracy.history.dto.IssueHistoryDto;
import seoul.democracy.history.dto.IssueHistoryUpdateDto;
import seoul.democracy.history.predicate.IssueHistoryPredicate;
import seoul.democracy.history.service.IssueHistoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/ajax/histories")
public class AdminHistoryAjaxController {

    private final IssueHistoryService historyService;

    @Autowired
    public AdminHistoryAjaxController(IssueHistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * 히스토리 작성
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResultInfo createHistory(@RequestBody @Valid IssueHistoryCreateDto createDto) {
        historyService.create(createDto);

        return ResultInfo.of("히스토리를 작성하였습니다.");
    }

    /**
     * 히스토리 수정
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public IssueHistoryDto getHistory(@PathVariable("id") Long id) {
        return historyService.getHistory(IssueHistoryPredicate.equalId(id), IssueHistoryDto.projectionForSite);
    }

    /**
     * 히스토리 수정
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResultInfo updateHistory(@RequestBody @Valid IssueHistoryUpdateDto updateDto) {
        historyService.update(updateDto);

        return ResultInfo.of("히스토리를 수정하였습니다.");
    }

    /**
     * 히스토리 삭제
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultInfo deleteHistory(@PathVariable("id") Long id) {
        historyService.delete(id);

        return ResultInfo.of("히스토리를 삭제하였습니다.");
    }
}
