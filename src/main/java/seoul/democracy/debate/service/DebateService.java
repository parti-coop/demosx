package seoul.democracy.debate.service;


import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateCreateDto;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.dto.DebateUpdateDto;
import seoul.democracy.debate.repository.DebateRepository;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.dto.IssueDto;
import seoul.democracy.issue.repository.CategoryRepository;
import seoul.democracy.issue.repository.IssueRepository;

import java.util.List;

import static seoul.democracy.issue.predicate.CategoryPredicate.equalName;
import static seoul.democracy.issue.predicate.IssuePredicate.equalIdIn;

@Service
@Transactional(readOnly = true)
public class DebateService {

    private final DebateRepository debateRepository;
    private final CategoryRepository categoryRepository;
    private final IssueRepository issueRepository;

    @Autowired
    public DebateService(DebateRepository debateRepository,
                         CategoryRepository categoryRepository,
                         IssueRepository issueRepository) {
        this.debateRepository = debateRepository;
        this.categoryRepository = categoryRepository;
        this.issueRepository = issueRepository;
    }

    public DebateDto getDebate(Predicate predicate, Expression<DebateDto> projection, boolean withFiles, boolean withRelations) {
        DebateDto debateDto = debateRepository.findOne(predicate, projection, withFiles, withRelations);
        if (withRelations && debateDto.getRelations().size() > 0) {
            List<IssueDto> issues = issueRepository.findAll(equalIdIn(debateDto.getRelations()), IssueDto.projectionForBasic);
            debateDto.setIssues(issues);
        }
        return debateDto;
    }

    public Page<DebateDto> getDebates(Predicate predicate, Pageable pageable, Expression<DebateDto> projection, boolean withFiles, boolean withRelations) {
        return debateRepository.findAll(predicate, pageable, projection, withFiles, withRelations);
    }

    private Category getCategory(String categoryName) {
        Category category = categoryRepository.findOne(equalName(categoryName));
        if (category == null || !category.getEnabled())
            throw new BadRequestException("category", "error.category", "카테고리를 확인해 주세요.");

        return category;
    }

    /**
     * 토론 등록
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Debate create(DebateCreateDto createDto, String ip) {
        Category category = getCategory(createDto.getCategory());

        Debate debate = Debate.create(createDto, category, ip);

        return debateRepository.save(debate);
    }

    /**
     * 토론 수정
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Debate update(DebateUpdateDto updateDto, String ip) {
        Debate debate = debateRepository.findOne(updateDto.getId());
        if (debate == null)
            throw new NotFoundException("해당 토론을 찾을 수 없습니다.");

        Category category = debate.getCategory().getName().equals(updateDto.getCategory()) ?
                                debate.getCategory() : getCategory(updateDto.getCategory());

        return debate.update(updateDto, category, ip);
    }
}
