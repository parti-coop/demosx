package seoul.democracy.social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.dto.UserDetail;
import seoul.democracy.user.service.UserService;

import java.util.Collection;
import java.util.Collections;

@Service
public class SocialUserDetailService {

    private final UserService userService;
    private final RoleHierarchy roleHierarchy;

    @Autowired
    public SocialUserDetailService(UserService userService, RoleHierarchy roleHierarchy) {
        this.userService = userService;
        this.roleHierarchy = roleHierarchy;
    }


    public UserDetails loadUserByUsername(String provider, String id, String name, String photo) throws UsernameNotFoundException {
        User user = userService.getUserBySocial(provider, id, name, photo);

        Collection<? extends GrantedAuthority> authorities = roleHierarchy.getReachableGrantedAuthorities(
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );

        return new UserDetail(user, authorities);
    }
}
