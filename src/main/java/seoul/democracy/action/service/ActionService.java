package seoul.democracy.action.service;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.action.domain.Action;
import seoul.democracy.action.dto.ActionCreateDto;
import seoul.democracy.action.dto.ActionDto;
import seoul.democracy.action.dto.ActionUpdateDto;
import seoul.democracy.action.repository.ActionRepository;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.service.CategoryService;

@Service
@Transactional(readOnly = true)
public class ActionService {

    private final ActionRepository actionRepository;
    final private CategoryService categoryService;

    @Autowired
    public ActionService(ActionRepository actionRepository,
                         CategoryService categoryService) {
        this.actionRepository = actionRepository;
        this.categoryService = categoryService;
    }

    public Page<ActionDto> getActions(Predicate predicate, Pageable pageable, Expression<ActionDto> projection, boolean withFiles, boolean withRelations) {
        return actionRepository.findAll(predicate, pageable, projection, withFiles, withRelations);
    }

    public ActionDto getAction(Predicate predicate, Expression<ActionDto> projection, boolean withFiles, boolean withRelations) {
        return actionRepository.findOne(predicate, projection, withFiles, withRelations);
    }

    /**
     * 실행 등록
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Action create(ActionCreateDto createDto) {
        Action action = Action.create(createDto, categoryService.getCategory(createDto.getCategory()));

        return actionRepository.save(action);
    }

    /**
     * 실행 수정
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Action update(ActionUpdateDto updateDto) {
        Action action = actionRepository.findOne(updateDto.getId());
        if (action == null) throw new NotFoundException("해당 실행을 찾을 수 없습니다.");

        return action.update(updateDto, categoryService.getCategory(updateDto.getCategory()));
    }

}
