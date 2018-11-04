package seoul.democracy.debate.service;


import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.debate.domain.Debate;
import seoul.democracy.debate.dto.DebateCreateDto;
import seoul.democracy.debate.dto.DebateDto;
import seoul.democracy.debate.repository.DebateRepository;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.predicate.CategoryPredicate;
import seoul.democracy.issue.repository.CategoryRepository;
import seoul.democracy.issue.repository.IssueRepository;

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
        return debateRepository.findOne(predicate, projection, withFiles, withRelations);
    }

    /**
     * 토론 등록
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Debate create(DebateCreateDto createDto, String ip) {
        Category category = categoryRepository.findOne(CategoryPredicate.equalName(createDto.getCategory()));
        if (category == null || !category.getEnabled())
            throw new BadRequestException("category", "error.category", "카테고리를 확인해 주세요.");

        Debate debate = Debate.create(createDto, category, ip);

        return debateRepository.save(debate);
    }

}
