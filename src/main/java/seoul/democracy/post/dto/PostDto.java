package seoul.democracy.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.post.domain.Post;
import seoul.democracy.post.domain.PostType;
import seoul.democracy.user.dto.UserDto;

import java.time.LocalDateTime;

import static seoul.democracy.post.domain.QPost.post;

@Data
public class PostDto {

    public final static QBean<PostDto> projection = Projections.fields(PostDto.class,
        post.id, post.createdDate, post.modifiedDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        UserDto.projectionForBasicByModifiedBy.as("modifiedBy"),
        post.createdIp, post.modifiedIp,
        post.type, post.status, post.title, post.content);

    /**
     * 관리자 게시판 리스트에서 사용
     */
    public final static QBean<PostDto> projectionForAdminList = Projections.fields(PostDto.class,
        post.id, post.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        post.type, post.status, post.title);

    /**
     * 관리자 게시판 상세에서 사용
     */
    public final static QBean<PostDto> projectionForAdminDetail = Projections.fields(PostDto.class,
        post.id, post.createdDate,
        UserDto.projectionForBasicByCreatedBy.as("createdBy"),
        post.type, post.status, post.title, post.content);

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UserDto createdBy;
    private UserDto modifiedBy;
    private String createdIp;
    private String modifiedIp;

    private PostType type;
    private Post.Status status;
    private String title;
    private String content;

}
