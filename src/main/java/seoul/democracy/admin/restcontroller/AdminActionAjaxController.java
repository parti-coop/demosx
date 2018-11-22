package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoul.democracy.action.dto.ActionDto;
import seoul.democracy.action.service.ActionService;

import static seoul.democracy.action.dto.ActionDto.projectionForAdminList;
import static seoul.democracy.action.predicate.ActionPredicate.getPredicateForAdminList;

@RestController
@RequestMapping("/admin/ajax/issue/actions")
public class AdminActionAjaxController {

    private final ActionService actionService;

    @Autowired
    public AdminActionAjaxController(ActionService actionService) {
        this.actionService = actionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<ActionDto> getActions(@RequestParam("search") String search,
                                      @RequestParam(value = "category", required = false) String category,
                                      @PageableDefault Pageable pageable) {
        return actionService.getActions(getPredicateForAdminList(search, category), pageable, projectionForAdminList);
    }

}
