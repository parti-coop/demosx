package seoul.democracy.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;
import seoul.democracy.user.domain.Role;
import seoul.democracy.user.domain.User;

import java.time.LocalDateTime;

import static seoul.democracy.user.domain.QUser.user;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    public final static QBean<UserDto> projection = Projections.fields(UserDto.class,
        user.id, user.createdDate, user.role, user.status, user.email, user.name, user.photo, user.loginDate,
        user.department.department1.as("department1"), user.department.department2.as("department2"), user.department.department3.as("department3"));

    public final static QBean<UserDto> projectionForAdminList = Projections.fields(UserDto.class,
        user.id, user.createdDate, user.role, user.status, user.email, user.name, user.loginDate);

    //public final static QBean<UserDto> projectionForMe = Projections.fields(UserDto.class, user.id, user.email, user.role);
    //public final static QBean<UserDto> projectionForBasic = Projections.fields(UserDto.class, user.email, user.name);

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    private Role role;
    private User.Status status;
    private String email;
    private String name;
    private String photo;
    private LocalDateTime loginDate;

    private String department1;
    private String department2;
    private String department3;

    public String getName() {
        return status == User.Status.DEACTIVATED ? "탈퇴회원" : name;
    }
}
