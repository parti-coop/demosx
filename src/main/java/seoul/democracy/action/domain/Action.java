package seoul.democracy.action.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import seoul.democracy.action.dto.ActionCreateDto;
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
                  List<IssueFile> files, List<IssueRelation> relations, String ip) {
        this.stats = IssueStats.create();
        this.status = status;
        this.category = category;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.createdIp = this.modifiedIp = ip;

        this.files = files;
        this.relations = relations;
    }

    public static Action create(ActionCreateDto createDto, Category category, String ip) {
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
            createDto.getStatus(), files, relations, ip);
    }

    @Override
    public Opinion createOpinion(OpinionCreateDto createDto, String ip) {
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
