package group.bison.netty.modules.oauth2.jwt;

import group.bison.netty.modules.oauth2.tools.TokenEncodeTool;
import group.bison.netty.modules.oauth2.tools.JsonUtil;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if(StringUtils.countOccurrencesOf(accessToken.getValue(), ".") != 0) {
            return accessToken.getValue();
        }

        String token = JsonUtil.toJson((Map<String, Object>) getAccessTokenConverter().convertAccessToken(accessToken, authentication));
        return String.join("", "Basic", token);
    }

    @Override
    protected Map<String, Object> decode(String token) {
        if(StringUtils.countOccurrencesOf(token, ".") == 0) {
            return Collections.emptyMap();
        }
        return JsonUtil.fromJson(token.replace("Basic", ""), HashMap.class);
    }
}
