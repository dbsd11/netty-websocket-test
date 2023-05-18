package group.bison.netty.modules.oauth2.security.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * CustomRedisStore
 *
 * @author xuan
 * @date 2018/9/2
 */
@Slf4j
public class CustomTokenStore implements TokenStore {

    private CustomRedisTokenStore customRedisTokenStore;

    private JdbcTokenStore jdbcTokenStore;

    public void setCustomRedisTokenStore(CustomRedisTokenStore customRedisTokenStore) {
        this.customRedisTokenStore = customRedisTokenStore;
    }

    public void setJdbcTokenStore(JdbcTokenStore jdbcTokenStore) {
        this.jdbcTokenStore = jdbcTokenStore;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication oAuth2Authentication = customRedisTokenStore.readAuthentication(token);
        if (oAuth2Authentication == null) {
            oAuth2Authentication = jdbcTokenStore.readAuthentication(token);
        }
        return oAuth2Authentication;
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        OAuth2Authentication oAuth2Authentication = customRedisTokenStore.readAuthentication(token);
        if (oAuth2Authentication == null) {
            oAuth2Authentication = jdbcTokenStore.readAuthentication(token);
        }
        return oAuth2Authentication;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        customRedisTokenStore.storeAccessToken(token, authentication);
        try {
            jdbcTokenStore.storeAccessToken(token, authentication);
        } catch (Exception e) {
            log.debug("jdbcTokenStore exception", e);
        }
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken oAuth2AccessToken = customRedisTokenStore.readAccessToken(tokenValue);
        if (oAuth2AccessToken == null) {
            oAuth2AccessToken = jdbcTokenStore.readAccessToken(tokenValue);
        }
        return oAuth2AccessToken;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        customRedisTokenStore.removeAccessToken(token);
        try {
            jdbcTokenStore.removeAccessToken(token);
        } catch (Exception e) {
            log.debug("jdbcTokenStore exception", e);
        }
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        customRedisTokenStore.storeRefreshToken(refreshToken, authentication);
        try {
            jdbcTokenStore.storeRefreshToken(refreshToken, authentication);
        } catch (Exception e) {
            log.debug("jdbcTokenStore exception", e);
        }
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        OAuth2RefreshToken oAuth2RefreshToken = customRedisTokenStore.readRefreshToken(tokenValue);
        if (oAuth2RefreshToken == null) {
            oAuth2RefreshToken = jdbcTokenStore.readRefreshToken(tokenValue);
        }
        return oAuth2RefreshToken;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        OAuth2Authentication oAuth2Authentication = customRedisTokenStore.readAuthenticationForRefreshToken(token);
        if (oAuth2Authentication == null) {
            oAuth2Authentication = jdbcTokenStore.readAuthenticationForRefreshToken(token);
        }
        return oAuth2Authentication;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        customRedisTokenStore.removeRefreshToken(token);
        try {
            jdbcTokenStore.removeRefreshToken(token);
        } catch (Exception e) {
            log.debug("jdbcTokenStore exception", e);
        }
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        customRedisTokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
        try {
            jdbcTokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
        } catch (Exception e) {
            log.debug("jdbcTokenStore exception", e);
        }
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken oAuth2AccessToken = customRedisTokenStore.getAccessToken(authentication);
        if (oAuth2AccessToken == null) {
            oAuth2AccessToken = jdbcTokenStore.getAccessToken(authentication);
        }
        return oAuth2AccessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        Collection<OAuth2AccessToken> oAuth2AccessTokens = customRedisTokenStore.findTokensByClientIdAndUserName(clientId, userName);
        if (CollectionUtils.isEmpty(oAuth2AccessTokens)) {
            oAuth2AccessTokens = jdbcTokenStore.findTokensByClientIdAndUserName(clientId, userName);
        }
        return oAuth2AccessTokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        Collection<OAuth2AccessToken> oAuth2AccessTokens = customRedisTokenStore.findTokensByClientId(clientId);
        if (CollectionUtils.isEmpty(oAuth2AccessTokens)) {
            oAuth2AccessTokens = jdbcTokenStore.findTokensByClientId(clientId);
        }
        return oAuth2AccessTokens;
    }
}
