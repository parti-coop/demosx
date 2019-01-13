package seoul.democracy.social.service;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import seoul.democracy.common.util.JsonUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SocialAuthenticationProvider implements AuthenticationProvider {

    @Setter
    private SocialUserDetailService userDetailsService;

    @Setter
    private SocialService socialService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SocialAuthenticationToken authToken = (SocialAuthenticationToken) authentication;

        OAuth20Service service = socialService.naver();
        String id = null;
        String name = null;
        String photo = null;

        if (authToken.getProvider().equals("naver")) {
            final OAuthRequest req = new OAuthRequest(Verb.GET, "https://openapi.naver.com/v1/nid/me");
            service.signRequest((OAuth2AccessToken) authToken.getToken(), req);
            try {
                final Response res = service.execute(req);
                Map<String, Object> map = JsonUtils.asStringToMap(res.getBody());
                Map<String, Object> mapResponse = (Map<String, Object>) map.get("response");
                id = (String) mapResponse.get("id");
                name = (String) mapResponse.get("name");
                photo = (String) mapResponse.get("profile_image");
            } catch (InterruptedException | ExecutionException | IOException e) {
                throw new UsernameNotFoundException("Unknown connectd account id");
            }
        }

        if (id == null || name == null) throw new UsernameNotFoundException("Unknown connectd account id");

        UserDetails userDetail = userDetailsService.loadUserByUsername(authToken.getProvider(), id, name, photo);
        if (userDetail == null) throw new UsernameNotFoundException("Unknown connectd account id");

        return new UsernamePasswordAuthenticationToken(
            userDetail, authentication.getCredentials(),
            userDetail.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SocialAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
