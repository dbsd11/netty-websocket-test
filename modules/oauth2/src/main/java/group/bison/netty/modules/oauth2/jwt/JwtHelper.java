package group.bison.netty.modules.oauth2.jwt;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import reactor.core.publisher.Mono;

@Component
public class JwtHelper implements ReactiveJwtDecoder, InitializingBean {

    private static JwtHelper INSTANCE = null;

    private final static String TOKEN_PREFIX = "Bearer";
    public final static String SECRET = "xxxxxxxxxx";

    public Map<String, Object> validateTokenAndGetClaims(String token) {
        try {
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            return body;
        } catch (Exception ex) {
            return new HashMap<>();
        }
    }

    // 解析token
    public Map<String, Object> parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    // 创建token
    public String createToken(Map<String, Object> claims, long expiration) {
        Date date = Date.from(Instant.now().plusSeconds(expiration));
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return jwt;
    }

    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        Jwt jwt = null;
        try {
            Jws<Claims> jws = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));

                Jwt.Builder jwtBuilder = Jwt.withTokenValue(token);
                for(Map.Entry entry : ((Set<Map.Entry>)jws.getHeader().entrySet())) {
                    jwtBuilder.header(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }

                for(Map.Entry<String, Object> entry : ((Set<Map.Entry<String, Object>>)jws.getBody().entrySet())) {
                    if((StringUtils.equalsIgnoreCase(entry.getKey(), JwtClaimNames.IAT) || StringUtils.equalsIgnoreCase(entry.getKey(), JwtClaimNames.EXP)) && entry.getValue() != null) {
                        jwtBuilder.claim(String.valueOf(entry.getKey()), Instant.ofEpochSecond((Integer)entry.getValue()));
                    } else {
                        jwtBuilder.claim(String.valueOf(entry.getKey()), entry.getValue());
                    }
                }
                jwt = jwtBuilder.build();
        } catch (Exception e) {
            throw new JwtException("decode failed", e);
        }
        
        return Mono.justOrEmpty(jwt);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
        
        createTestToken();
    }

    void createTestToken() {
        String testToken = createToken(new HashMap(Collections.singletonMap("sub", "test01")), 24 * 60 * 60 * 1000);
        System.out.println("test jwt token: " +  testToken);
    }

    public static JwtHelper getInstance() {
        return INSTANCE;
    }
}
