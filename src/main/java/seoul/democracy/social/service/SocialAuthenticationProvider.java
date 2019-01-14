package seoul.democracy.social.service;

import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth10aService;
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

        String id = null;
        String name = null;
        String photo = null;

        if (authToken.getProvider().equals("naver")) {
            OAuth20Service service = socialService.naver();
            final OAuthRequest req = new OAuthRequest(Verb.GET, "https://openapi.naver.com/v1/nid/me");
            service.signRequest((OAuth2AccessToken) authToken.getToken(), req);
            try {
                final Response res = service.execute(req);
                Map<String, Object> map = JsonUtils.asStringToMap(res.getBody());
                Map<String, Object> mapResponse = (Map<String, Object>) map.get("response");
                id = mapResponse.get("id").toString();
                name = mapResponse.get("name").toString();
                photo = mapResponse.get("profile_image").toString();
            } catch (InterruptedException | ExecutionException | IOException e) {
                throw new UsernameNotFoundException("Unknown connectd account id");
            }
        } else if (authToken.getProvider().equals("kakao")) {
            OAuth20Service service = socialService.kakao();
            final OAuthRequest req = new OAuthRequest(Verb.GET, "https://kapi.kakao.com/v2/user/me");
            service.signRequest((OAuth2AccessToken) authToken.getToken(), req);
            try {
                final Response res = service.execute(req);
                Map<String, Object> map = JsonUtils.asStringToMap(res.getBody());
                Map<String, Object> mapResponse = (Map<String, Object>) map.get("properties");
                id = map.get("id").toString();
                name = mapResponse.get("nickname").toString();
                photo = mapResponse.get("profile_image").toString();
            } catch (InterruptedException | ExecutionException | IOException e) {
                throw new UsernameNotFoundException("Unknown connectd account id");
            }
        } else if (authToken.getProvider().equals("twitter")) {
            OAuth10aService service = socialService.twitter();
            final OAuthRequest req = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");
            service.signRequest((OAuth1AccessToken) authToken.getToken(), req);
            try {
                final Response res = service.execute(req);
                Map<String, Object> map = JsonUtils.asStringToMap(res.getBody());
                id = map.get("id").toString();
                name = map.get("name").toString();
                photo = map.get("profile_image_url_https").toString();
            } catch (InterruptedException | ExecutionException | IOException e) {
                throw new UsernameNotFoundException("Unknown connectd account id");
            }
        } else if (authToken.getProvider().equals("facebook")) {
            OAuth20Service service = socialService.facebook();
            final OAuthRequest req = new OAuthRequest(Verb.GET, "https://graph.facebook.com/v2.11/me?fields=id,name,picture.type(large)");
            service.signRequest((OAuth2AccessToken) authToken.getToken(), req);
            try {
                final Response res = service.execute(req);
                Map<String, Object> map = JsonUtils.asStringToMap(res.getBody());
                id = map.get("id").toString();
                name = map.get("name").toString();
                photo = ((Map)((Map)map.get("picture")).get("data")).get("url").toString();
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
