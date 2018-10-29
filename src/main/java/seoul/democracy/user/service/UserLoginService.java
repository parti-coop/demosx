package seoul.democracy.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.dto.UserDetail;
import seoul.democracy.user.predicate.UserPredicate;
import seoul.democracy.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserLoginService implements UserDetailsService {

    final private UserRepository userRepository;
    final private RoleHierarchy roleHierarchy;

    @Autowired
    public UserLoginService(UserRepository userRepository, RoleHierarchy roleHierarchy) {
        this.userRepository = userRepository;
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findOne(UserPredicate.equalEmail(email));
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        Collection<? extends GrantedAuthority> authorities = roleHierarchy.getReachableGrantedAuthorities(
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );

        return new UserDetail(user, authorities);
    }
}
