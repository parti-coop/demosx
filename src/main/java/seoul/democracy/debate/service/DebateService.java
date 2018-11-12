package seoul.democracy.debate.service;


import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateCreateDto;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.dto.DebateUpdateDto;
import seoul.democracy.debate.repository.DebateRepository;
import seoul.democracy.issue.domain.IssueGroup;
import seoul.democracy.issue.service.CategoryService;

@Service
@Transactional(readOnly = true)
public class DebateService {

    private final DebateRepository debateRepository;
    final private CategoryService categoryService;

    @Autowired
    public DebateService(DebateRepository debateRepository,
                         CategoryService categoryService) {
        this.debateRepository = debateRepository;
        this.categoryService = categoryService;
    }

    public DebateDto getDebate(Predicate predicate, Expression<DebateDto> projection, boolean withFiles, boolean withRelations) {
        return debateRepository.findOne(predicate, projection, withFiles, withRelations);
    }

    public Page<DebateDto> getDebates(Predicate predicate, Pageable pageable, Expression<DebateDto> projection, boolean withFiles, boolean withRelations) {
        return debateRepository.findAll(predicate, pageable, projection, withFiles, withRelations);
    }

    /**
     * 토론 등록
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Debate create(IssueGroup group, DebateCreateDto createDto) {
        Debate debate = Debate.create(group, createDto, categoryService.getCategory(createDto.getCategory()));

        return debateRepository.save(debate);
    }

    /**
     * 토론 수정
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Debate update(DebateUpdateDto updateDto) {
        Debate debate = debateRepository.findOne(updateDto.getId());
        if (debate == null)
            throw new NotFoundException("해당 토론을 찾을 수 없습니다.");

        return debate.update(updateDto, categoryService.getCategory(updateDto.getCategory()));
    }
}
