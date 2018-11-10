package seoul.democracy.action.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import seoul.democracy.action.dto.ActionCreateDto;
import seoul.democracy.action.dto.ActionUpdateDto;
import seoul.democracy.issue.domain.*;
import seoul.democracy.issue.dto.IssueFileDto;
import seoul.democracy.opinion.domain.Opinion;
import seoul.democracy.opinion.dto.OpinionCreateDto;

import javax.persistence.*;
import java.util.ArrayList;
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
        // todo 정리 필요, update 부분과 같이
        List<IssueFile> files = new ArrayList<>();
        if (createDto.getFiles() != null) {
            for (int i = 0; i < createDto.getFiles().size(); i++) {
                IssueFileDto fileDto = createDto.getFiles().get(i);
                files.add(IssueFile.of(i, fileDto.getName(), fileDto.getUrl()));
            }
        }
        List<IssueRelation> relations = new ArrayList<>();
        if (createDto.getRelations() != null) {
            for (int i = 0; i < createDto.getRelations().size(); i++) {
                relations.add(IssueRelation.create(i, createDto.getRelations().get(i)));
            }
        }

        return new Action(category, createDto.getThumbnail(), createDto.getTitle(), createDto.getContent(),
            createDto.getStatus(), files, relations);
    }

    public Action update(ActionUpdateDto updateDto, Category category) {
        this.category = category;
        this.thumbnail = updateDto.getThumbnail();
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        this.status = updateDto.getStatus();

        List<IssueFile> files = new ArrayList<>();
        if (updateDto.getFiles() != null) {
            for (int i = 0; i < updateDto.getFiles().size(); i++) {
                IssueFileDto fileDto = updateDto.getFiles().get(i);
                files.add(IssueFile.of(i, fileDto.getName(), fileDto.getUrl()));
            }
        }
        this.files = files;

        List<IssueRelation> relations = new ArrayList<>();
        if (updateDto.getFiles() != null) {
            for (int i = 0; i < updateDto.getRelations().size(); i++) {
                relations.add(IssueRelation.create(i, updateDto.getRelations().get(i)));
            }
        }
        this.relations = relations;

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
