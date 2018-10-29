package seoul.democracy.user.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import seoul.democracy.user.domain.User;

import java.util.Collection;

@Getter
public class UserDetail extends org.springframework.security.core.userdetails.User {

    final private User user;

    public UserDetail(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);

        this.user = user;
    }
}
