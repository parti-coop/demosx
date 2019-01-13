package seoul.democracy.social.service;

import com.github.scribejava.apis.NaverApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
public class SocialService {
    private final static String SESSION_STATE = "auth_state";

    private final static String naverClientId = "sYuwq_WfJWKg8p73jpPY";
    private final static String naverClientSecret = "zgsokxptmG";
    private final static String naverRedirectUrl = "http://localhost:8091/auth?p=naver";


    /* 세션 유효성 검증을 위한 난수 생성기 */
    private String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    /* http session에 데이터 저장 */
    private void setSession(HttpSession session, String state) {
        session.setAttribute(SESSION_STATE, state);
    }

    /* http session에서 데이터 가져오기 */
    private String getSession(HttpSession session) {
        return (String) session.getAttribute(SESSION_STATE);
    }

    //
    public OAuth20Service naver(HttpSession session) {
        String state = generateRandomString();
        setSession(session, state);

        return new ServiceBuilder(naverClientId)
                   .apiSecret(naverClientSecret)
                   .callback(naverRedirectUrl)
                   .state(state) // callback에서 이 값을 비교하여 유효한 결과인지 확인
                   .build(NaverApi.instance());
    }

    public OAuth20Service naver() {
        return new ServiceBuilder(naverClientId)
                   .apiSecret(naverClientSecret)
                   .callback(naverRedirectUrl)
                   .build(NaverApi.instance());
    }
}
