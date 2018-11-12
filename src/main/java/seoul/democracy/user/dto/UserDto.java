package seoul.democracy.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import org.springframework.util.StringUtils;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.user.domain.QUser;
import seoul.democracy.user.domain.Role;
import seoul.democracy.user.domain.User;

import java.time.LocalDateTime;

import static seoul.democracy.user.domain.QUser.user;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    public final static QUser createdBy = new QUser("createdBy");
    public final static QUser modifiedBy = new QUser("modifiedBy");

    public final static QBean<UserDto> projection = Projections.fields(UserDto.class,
        user.id, user.createdDate, user.role, user.status, user.email, user.name, user.photo, user.loginDate, user.loginIp,
        CategoryDto.projectionForFilter.as("category"),
        user.department.department1.as("department1"), user.department.department2.as("department2"), user.department.department3.as("department3"));

    public final static QBean<UserDto> projectionForAdminList = Projections.fields(UserDto.class,
        user.id, user.createdDate, user.role, user.status, user.email, user.name, user.loginDate, user.loginIp);

    public final static QBean<UserDto> projectionForAdminManager = Projections.fields(UserDto.class,
        user.id, user.role, user.status, user.email, user.name,
        CategoryDto.projectionForFilter.as("category"),
        user.department.department1.as("department1"), user.department.department2.as("department2"), user.department.department3.as("department3"));

    public final static QBean<UserDto> projectionForBasic = Projections.fields(UserDto.class, user.id, user.email, user.name, user.photo);
    public final static QBean<UserDto> projectionForBasicByCreatedBy = Projections.fields(UserDto.class, createdBy.id, createdBy.email, createdBy.name, createdBy.photo);
    public final static QBean<UserDto> projectionForBasicByModifiedBy = Projections.fields(UserDto.class, modifiedBy.id, modifiedBy.email, modifiedBy.name);

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    private Role role;
    private User.Status status;
    private String email;
    private String name;
    private String photo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime loginDate;
    private String loginIp;

    private CategoryDto category;
    private String department1;
    private String department2;
    private String department3;

    public String getName() {
        return status == User.Status.DEACTIVATED ? "탈퇴회원" : name;
    }

    public String viewPhoto() {
        return StringUtils.hasText(photo) ? photo : "/images/noavatar.png";
    }
}
