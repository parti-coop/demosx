package seoul.democracy.debate.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import seoul.democracy.common.converter.LocalDateAttributeConverter;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.debate.dto.DebateCreateDto;
import seoul.democracy.debate.dto.DebateUpdateDto;
import seoul.democracy.issue.domain.*;
import seoul.democracy.opinion.domain.DebateOpinion;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.opinion.domain.ProposalOpinion;
import seoul.democracy.opinion.dto.OpinionCreateDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("D")
public class Debate extends Issue {

    /**
     * 이슈 과정
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ISSUE_PROCESS")
    private Process process;

    /**
     * 이미지 URL
     */
    @Column(name = "IMG_URL")
    private String thumbnail;

    /**
     * 토론 시작일
     */
    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "DEBATE_STDATE")
    private LocalDate startDate;

    /**
     * 토론 종료일
     */
    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "DEBATE_EDDATE")
    private LocalDate endDate;

    private Debate(IssueGroup group, Category category, String thumbnail, OpinionType opinionType,
                   LocalDate startDate, LocalDate endDate,
                   String title, String excerpt, String content, Status status,
                   List<IssueFile> files, List<IssueRelation> relations) {
        this.group = group;
        this.stats = IssueStats.create();
        this.status = status;
        this.category = category;
        this.opinionType = opinionType;
        this.thumbnail = thumbnail;
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;

        updateDate(startDate, endDate);

        this.files = files;
        this.relations = relations;
    }

    private void updateDate(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        LocalDate now = LocalDate.now();
        if (now.isBefore(this.startDate)) this.process = Process.INIT;
        else if (now.isAfter(this.endDate)) this.process = Process.COMPLETE;
        else this.process = Process.PROGRESS;
    }

    public static Debate create(IssueGroup group, DebateCreateDto createDto, Category category) {
        return new Debate(group, category, createDto.getThumbnail(), createDto.getOpinionType(),
            createDto.getStartDate(), createDto.getEndDate(),
            createDto.getTitle(), createDto.getExcerpt(), createDto.getContent(), createDto.getStatus(),
            IssueFile.create(createDto.getFiles()), IssueRelation.create(createDto.getRelations()));
    }

    public Debate update(DebateUpdateDto updateDto, Category category) {
        this.status = updateDto.getStatus();
        this.category = category;
        this.thumbnail = updateDto.getThumbnail();
        this.title = updateDto.getTitle();
        this.excerpt = updateDto.getExcerpt();
        this.content = updateDto.getContent();

        updateDate(updateDto.getStartDate(), updateDto.getEndDate());

        updateFiles(updateDto.getFiles());
        updateRelations(updateDto.getRelations());

        return this;
    }

    @Override
    public Opinion createOpinion(OpinionCreateDto createDto) {
        if (!process.isProgress())
            throw new BadRequestException("process", "error.progress", "토론 진행상태가 아닙니다.");

        return opinionType == OpinionType.PROPOSAL ?
                   ProposalOpinion.create(this, createDto.getContent()) :
                   DebateOpinion.create(this, createDto.getVote(), createDto.getContent());
    }

    @Override
    public boolean isUpdatableOpinion() {
        return status.isOpen() && process.isProgress();
    }

    @Getter
    @RequiredArgsConstructor
    public enum Process {
        INIT("진행 예정"),
        PROGRESS("진행 중"),       // 진행 중
        COMPLETE("진행 완료");       // 토론 종료

        private final String msg;

        public boolean isProgress() {
            return this == PROGRESS;
        }

        public boolean isComplete() {
            return this == COMPLETE;
        }
    }
}
