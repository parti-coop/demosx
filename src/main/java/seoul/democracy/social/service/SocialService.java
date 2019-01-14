package seoul.democracy.social.service;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.NaverApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.stereotype.Service;
import seoul.democracy.social.api.KakaoApi;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class SocialService {
    private final static String SESSION_STATE = "auth_state";

    private final static String naverClientId = "sYuwq_WfJWKg8p73jpPY";
    private final static String naverClientSecret = "zgsokxptmG";
    private final static String naverRedirectUrl = "http://localhost:8091/auth/naver";

    private final static String kakaoClientId = "a7e7431647799a92c02afdc4f7ffd758";
    private final static String kakaoRedirectUrl = "http://localhost:8091/auth/kakao";

    private final static String twitterClientId = "wtKXAJ6nYMMbcRDmbFOmVKWBr";
    private final static String twitterClientSecret = "nNg7ftttJYnTYQsr7mG6NMrFmKGZh4kdDu9gwmRtH5scd0JWiC";
    private final static String twitterRedirectUrl = "http://127.0.0.1:8091/auth/twitter";

    private final static String facebookClientId = "804803166523275";
    private final static String facebookClientSecret = "7c3d4abbd668661f81d2936dc0f1872a";
    private final static String facebookRedirectUrl = "http://localhost:8091/auth/facebook";

    /* 세션 유효성 검증을 위한 난수 생성기 */
    private String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    /* http session에 데이터 저장 */
    private void setSession(HttpSession session, Object state) {
        session.setAttribute(SESSION_STATE, state);
    }

    /* http session에서 데이터 가져오기 */
    public Object getSession(HttpSession session) {
        return session.getAttribute(SESSION_STATE);
    }

    public String naverAuthorizationUrl(HttpSession session) {
        String state = generateRandomString();
        setSession(session, state);

        return new ServiceBuilder(naverClientId)
                   .apiSecret(naverClientSecret)
                   .callback(naverRedirectUrl)
                   .state(state) // callback에서 이 값을 비교하여 유효한 결과인지 확인
                   .build(NaverApi.instance())
                   .getAuthorizationUrl();
    }

    public OAuth20Service naver() {
        return new ServiceBuilder(naverClientId)
                   .apiSecret(naverClientSecret)
                   .callback(naverRedirectUrl)
                   .build(NaverApi.instance());
    }

    public String kakaoAuthorizationUrl(HttpSession session) {
        String state = generateRandomString();
        setSession(session, state);

        return new ServiceBuilder(kakaoClientId)
                   .state(state) // callback에서 이 값을 비교하여 유효한 결과인지 확인
                   .callback(kakaoRedirectUrl)
                   .debug()
                   .build(KakaoApi.instance())
                   .getAuthorizationUrl();
    }

    public OAuth20Service kakao() {
        return new ServiceBuilder(kakaoClientId)
                   .callback(kakaoRedirectUrl)
                   .build(KakaoApi.instance());
    }

    public String twitterAuthorizationUrl(HttpSession session) throws InterruptedException, ExecutionException, IOException {
        OAuth10aService service = new ServiceBuilder(twitterClientId)
                                      .apiSecret(twitterClientSecret)
                                      .callback(twitterRedirectUrl)
                                      .build(TwitterApi.instance());
        OAuth1RequestToken requestToken = service.getRequestToken();
        setSession(session, requestToken);

        return service.getAuthorizationUrl(requestToken);
    }

    public OAuth10aService twitter() {
        return new ServiceBuilder(twitterClientId)
                   .apiSecret(twitterClientSecret)
                   .callback(twitterRedirectUrl)
                   .build(TwitterApi.instance());
    }

    public String facebookAuthorizationUrl(HttpSession session) {
        String state = generateRandomString();
        setSession(session, state);

        return new ServiceBuilder(facebookClientId)
                   .apiSecret(facebookClientSecret)
                   .callback(facebookRedirectUrl)
                   .state(state) // callback에서 이 값을 비교하여 유효한 결과인지 확인
                   .build(FacebookApi.instance())
                   .getAuthorizationUrl();
    }

    public OAuth20Service facebook() {
        return new ServiceBuilder(facebookClientId)
                   .apiSecret(facebookClientSecret)
                   .callback(facebookRedirectUrl)
                   .build(FacebookApi.instance());
    }

}
