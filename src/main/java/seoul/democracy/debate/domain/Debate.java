package seoul.democracy.debate.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import seoul.democracy.common.converter.LocalDateAttributeConverter;
import seoul.democracy.common.exception.BadRequestException;
import seoul.democracy.debate.dto.DebateCreateDto;
import seoul.democracy.debate.dto.DebateUpdateDto;
import seoul.democracy.issue.domain.*;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.opinion.domain.DebateOpinion;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.domain.OpinionType;
import seoul.democracy.opinion.domain.ProposalOpinion;
import seoul.democracy.opinion.dto.OpinionCreateDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    /**
     * 연관 제안
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "TB_ISSUE_RELATION", joinColumns = {
        @JoinColumn(name = "ISSUE_ID", referencedColumnName = "ISSUE_ID")
    })
    private List<IssueRelation> relations = new ArrayList<>();

    private Debate(Category category, String thumbnail, OpinionType opinionType,
                   LocalDate startDate, LocalDate endDate,
                   String title, String content, Status status,
                   List<IssueFile> files, List<IssueRelation> relations, String ip) {
        this.stats = IssueStats.create();
        this.status = status;
        this.process = Process.INIT;
        this.category = category;
        this.opinionType = opinionType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.createdIp = this.modifiedIp = ip;

        this.files = files;
        this.relations = relations;
    }

    public static Debate create(DebateCreateDto createDto, Category category, String ip) {
        List<IssueFile> files = new ArrayList<>();
        for (int i = 0; i < createDto.getFiles().size(); i++) {
            IssueFileDto fileDto = createDto.getFiles().get(i);
            files.add(IssueFile.of(i, fileDto.getName(), fileDto.getUrl()));
        }
        List<IssueRelation> relations = new ArrayList<>();
        for (int i = 0; i < createDto.getRelations().size(); i++) {
            relations.add(IssueRelation.create(i, createDto.getRelations().get(i)));
        }
        return new Debate(category, createDto.getThumbnail(), createDto.getOpinionType(),
            createDto.getStartDate(), createDto.getEndDate(),
            createDto.getTitle(), createDto.getContent(), createDto.getStatus(),
            files, relations, ip);
    }

    public Debate update(DebateUpdateDto updateDto, Category category, String ip) {
        this.status = updateDto.getStatus();
        this.category = category;
        this.startDate = updateDto.getStartDate();
        this.endDate = updateDto.getEndDate();
        this.thumbnail = updateDto.getThumbnail();
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        this.modifiedIp = ip;

        List<IssueFile> files = new ArrayList<>();
        if(updateDto.getFiles() != null) {
            for (int i = 0; i < updateDto.getFiles().size(); i++) {
                IssueFileDto fileDto = updateDto.getFiles().get(i);
                files.add(IssueFile.of(i, fileDto.getName(), fileDto.getUrl()));
            }
        }
        this.files = files;

        List<IssueRelation> relations = new ArrayList<>();
        if(updateDto.getFiles() != null) {
            for (int i = 0; i < updateDto.getRelations().size(); i++) {
                relations.add(IssueRelation.create(i, updateDto.getRelations().get(i)));
            }
        }
        this.relations = relations;

        return this;
    }

    @Override
    public Opinion createOpinion(OpinionCreateDto createDto, String ip) {
        if (!process.isProgress())
            throw new BadRequestException("process", "error.progress", "토론 진행상태가 아닙니다.");

        return opinionType == OpinionType.PROPOSAL ?
                   ProposalOpinion.create(this, createDto.getContent(), ip) :
                   DebateOpinion.create(this, createDto.getVote(), createDto.getContent(), ip);
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
