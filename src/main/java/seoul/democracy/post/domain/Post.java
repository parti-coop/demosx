package seoul.democracy.post.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoul.democracy.common.annotation.CreatedIp;
import seoul.democracy.common.annotation.ModifiedIp;
import seoul.democracy.common.converter.LocalDateTimeAttributeConverter;
import seoul.democracy.common.listener.AuditingIpListener;
import seoul.democracy.post.dto.PostCreateDto;
import seoul.democracy.post.dto.PostUpdateDto;
import seoul.democracy.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 글
 */
@Getter
@NoArgsConstructor
@Entity(name = "TB_POST")
@EntityListeners({AuditingEntityListener.class, AuditingIpListener.class})
public class Post {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "POST_ID")
    private Long id;

    /**
     * 등록 일시
     */
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "REG_DT", insertable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * 수정 일시
     */
    @LastModifiedDate
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "CHG_DT", insertable = false)
    private LocalDateTime modifiedDate;

    /**
     * 등록 ID
     */
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REG_ID", updatable = false, nullable = false)
    private User createdBy;
    @Column(name = "REG_ID", insertable = false, updatable = false)
    private Long createdById;

    /**
     * 수정 ID
     */
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CHG_ID", nullable = false)
    private User modifiedBy;

    /**
     * 등록 아이피
     */
    @CreatedIp
    @Column(name = "REG_IP", updatable = false)
    private String createdIp;

    /**
     * 수정 아이피
     */
    @ModifiedIp
    @Column(name = "CHG_IP")
    private String modifiedIp;

    /**
     * 글 타입
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "POST_TYPE", updatable = false)
    private PostType type;

    /**
     * 글 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "POST_STATUS")
    private Status status;

    /**
     * 글 제목
     */
    @Column(name = "POST_TITLE")
    private String title;

    /**
     * 글 내용
     */
    @Lob
    @Column(name = "POST_CONTENT")
    private String content;

    private Post(PostType type, Status status, String title, String content) {
        this.type = type;
        this.status = status;
        this.title = title;
        this.content = content;
    }

    public static Post create(PostType type, PostCreateDto createDto) {
        return new Post(type, createDto.getStatus(), createDto.getTitle(), createDto.getContent());
    }

    public Post update(PostUpdateDto updateDto) {
        this.status = updateDto.getStatus();
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        return this;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        OPEN("공개"),       // 공개
        CLOSED("비공개");      // 비공개

        private final String msg;
    }

}
