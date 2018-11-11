package seoul.democracy.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seoul.democracy.common.dto.ResultInfo;
import seoul.democracy.issue.dto.CategoryCreateDto;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.CategoryUpdateDto;
import seoul.democracy.issue.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/ajax/categories")
public class AdminCategoryAjaxController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryAjaxController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CategoryDto> getCategories() {
        return categoryService.getCategories(null, CategoryDto.projection);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResultInfo create(@RequestBody @Valid CategoryCreateDto createDto) {
        categoryService.create(createDto);

        return ResultInfo.of("카테고리를 추가하였습니다.");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResultInfo update(@PathVariable("id") Long id,
                             @RequestBody @Valid CategoryUpdateDto updateDto) {
        categoryService.update(updateDto);

        return ResultInfo.of("카테고리를 수정하였습니다.");
    }
}
