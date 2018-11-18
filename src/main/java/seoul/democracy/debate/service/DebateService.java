package seoul.democracy.debate.service;


import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
import seoul.democracy.issue.service.IssueService;

import java.time.LocalDate;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DebateService {

    private final DebateRepository debateRepository;
    private final CategoryService categoryService;
    private final IssueService issueService;

    @Autowired
    public DebateService(DebateRepository debateRepository,
                         CategoryService categoryService,
                         IssueService issueService) {
        this.debateRepository = debateRepository;
        this.categoryService = categoryService;
        this.issueService = issueService;
    }

    public DebateDto getDebate(Predicate predicate, Expression<DebateDto> projection, boolean withFiles, boolean withRelations) {
        return debateRepository.findOne(predicate, projection, withFiles, withRelations);
    }

    public Page<DebateDto> getDebates(Predicate predicate, Pageable pageable, Expression<DebateDto> projection) {
        return debateRepository.findAll(predicate, pageable, projection);
    }

    /**
     * 토론 등록
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Debate create(IssueGroup group, DebateCreateDto createDto) {
        issueService.validateRelations(createDto.getRelations());

        Debate debate = Debate.create(group, createDto, categoryService.getCategory(createDto.getCategory()));

        return debateRepository.save(debate);
    }

    /**
     * 토론 수정
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Debate update(DebateUpdateDto updateDto) {
        issueService.validateRelations(updateDto.getRelations());

        Debate debate = debateRepository.findOne(updateDto.getId());
        if (debate == null)
            throw new NotFoundException("해당 토론을 찾을 수 없습니다.");

        return debate.update(updateDto, categoryService.getCategory(updateDto.getCategory()));
    }

    /**
     * 매일 자정마다 상태 변경 실행됨, 최초 서버 구동시 실행됨
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateDebateProcess() {
        LocalDate now = LocalDate.now();
        log.info("토론 상태 변경 처리 {}", now);

        debateRepository.updateDebateProcess();
    }

}
