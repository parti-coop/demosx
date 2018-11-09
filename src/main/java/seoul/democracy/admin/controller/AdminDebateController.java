package seoul.democracy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.dto.DebateUpdateDto;
import seoul.democracy.debate.predicate.DebatePredicate;
import seoul.democracy.debate.service.DebateService;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.service.CategoryService;

import javax.validation.Valid;
import java.net.InetAddress;
import java.util.List;

import static seoul.democracy.issue.dto.CategoryDto.projectionForFilter;
import static seoul.democracy.issue.predicate.CategoryPredicate.enabled;

@Controller
@RequestMapping("/admin/issue")
public class AdminDebateController {

    private final CategoryService categoryService;
    private final DebateService debateService;

    @Autowired
    public AdminDebateController(CategoryService categoryService,
                                 DebateService debateService) {
        this.categoryService = categoryService;
        this.debateService = debateService;
    }

    @ModelAttribute("categories")
    public List<CategoryDto> getCategories() {
        return categoryService.getCategories(enabled(), projectionForFilter);
    }

    /**
     * 관리자 > 시민제안 > 토론관리
     */
    @RequestMapping(value = "/debate.do", method = RequestMethod.GET)
    public String debateList() {
        return "/admin/debate/list";
    }

    /**
     * 관리자 > 시민제안 > 토론관리 > 상세
     */
    @RequestMapping(value = "/debate-detail.do", method = RequestMethod.GET)
    public String debateDetail(@RequestParam("id") Long id,
                               Model model) {

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(id), DebateDto.projection, true, true);
        model.addAttribute("debate", debateDto);

        return "/admin/debate/detail";
    }

    @RequestMapping(value = "/debate-edit.do", method = RequestMethod.GET)
    public String debateEdit(@RequestParam("id") Long id,
                             Model model) {

        DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(id), DebateDto.projection, true, true);
        model.addAttribute("debate", debateDto);

        DebateUpdateDto updateDto = DebateUpdateDto.of(debateDto);
        model.addAttribute("updateDto", updateDto);

        return "/admin/debate/update";
    }

    @RequestMapping(value = "/debate-edit.do", method = RequestMethod.POST)
    public String debateEdit(@RequestParam("id") Long id,
                             @ModelAttribute("updateDto") @Valid DebateUpdateDto updateDto,
                             BindingResult result,
                             Model model,
                             InetAddress address) {
        if(result.hasErrors()) {
            DebateDto debateDto = debateService.getDebate(DebatePredicate.equalId(id), DebateDto.projection, true, true);
            model.addAttribute("debate", debateDto);
            return "/admin/debate/update";
        }

        debateService.update(updateDto, address.getHostAddress());

        return "redirect:/admin/issue/debate.do";
    }
}
