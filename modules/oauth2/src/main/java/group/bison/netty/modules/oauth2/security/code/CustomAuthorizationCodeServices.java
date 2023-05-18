package group.bison.netty.modules.oauth2.security.code;

import group.bison.netty.modules.oauth2.tools.TokenEncodeTool;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

import java.util.Base64;

public class CustomAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private int codeValiditySeconds = 600; // default 10 min.

    private final RedisConnectionFactory connectionFactory;
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    private String prefix = "oauth2#code:";

    public CustomAuthorizationCodeServices(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void setSerializationStrategy(RedisTokenStoreSerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    @Override
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = super.createAuthorizationCode(authentication);
        code = TokenEncodeTool.getBase64CodeOrTokenWithEnv(code);
        store(code, authentication);
        return code;
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        boolean isBase64Code = false;
        try {
            isBase64Code = Base64.getDecoder().decode(code) != null;
        } catch (Exception e) {
        }
        if (!isBase64Code) {
            return;
        }

        RedisConnection conn = connectionFactory.getConnection();
        try {
            byte[] keyBytes = serializationStrategy.serialize(prefix + code);
            byte[] serializedAuth = serializationStrategy.serialize(authentication);
            conn.stringCommands().setEx(keyBytes, codeValiditySeconds, serializedAuth);
        } finally {
            conn.close();
        }
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        OAuth2Authentication authentication = null;
        RedisConnection conn = connectionFactory.getConnection();
        try {
            byte[] keyBytes = serializationStrategy.serialize(prefix + code);
            byte[] authenticationBytes = conn.stringCommands().get(keyBytes);
            if (authenticationBytes != null) {
                authentication = serializationStrategy.deserialize(authenticationBytes, OAuth2Authentication.class);
            }
            conn.expire(keyBytes, 10);
        } finally {
            conn.close();
        }
        return authentication;
    }
}
