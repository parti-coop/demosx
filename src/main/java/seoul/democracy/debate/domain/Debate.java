package seoul.democracy.debate.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.democracy.common.converter.LocalDateAttributeConverter;
import seoul.democracy.debate.dto.DebateCreateDto;
import seoul.democracy.issue.domain.*;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.opinion.domain.OpinionType;

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
        this.createdIp = ip;
        this.modifiedIp = ip;

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

    public enum Process {
        INIT,
        PROGRESS,       // 진행 중
        COMPLETE;       // 토론 종료

        public boolean isProgress() {
            return this == PROGRESS;
        }

        public boolean isComplete() {
            return this == COMPLETE;
        }
    }
}
