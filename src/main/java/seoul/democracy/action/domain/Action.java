package seoul.democracy.action.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import seoul.democracy.action.dto.ActionCreateDto;
import seoul.democracy.action.dto.ActionUpdateDto;
import seoul.democracy.issue.domain.*;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionCreateDto;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("A")
public class Action extends Issue {

    /**
     * 이슈 과정
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ISSUE_PROCESS", updatable = false)
    private Process process = Process.INIT;

    /**
     * 이미지 URL
     */
    @Column(name = "IMG_URL")
    private String thumbnail;

    public Action(Category category, String thumbnail, String title, String content, Status status,
                  List<IssueFile> files, List<IssueRelation> relations) {
        this.stats = IssueStats.create();
        this.status = status;
        this.category = category;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;

        this.files = files;
        this.relations = relations;
    }

    public static Action create(ActionCreateDto createDto, Category category) {
        return new Action(category, createDto.getThumbnail(), createDto.getTitle(), createDto.getContent(), createDto.getStatus(),
            IssueFile.create(createDto.getFiles()), IssueRelation.create(createDto.getRelations()));
    }

    public Action update(ActionUpdateDto updateDto, Category category) {
        this.category = category;
        this.thumbnail = updateDto.getThumbnail();
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        this.status = updateDto.getStatus();

        updateFiles(updateDto.getFiles());
        updateRelations(updateDto.getRelations());

        return this;
    }

    @Override
    public Opinion createOpinion(OpinionCreateDto createDto) {
        throw new UnsupportedOperationException("실행은 의견을 지원하지 않습니다.");
    }

    @Override
    public boolean isUpdatableOpinion() {
        return false;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Process {
        INIT
    }
}
