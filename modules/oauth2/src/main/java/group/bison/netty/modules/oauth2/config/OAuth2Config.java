package group.bison.netty.modules.oauth2.config;

import group.bison.netty.modules.oauth2.exception.CustomWebResponseExceptionTranslator;
import group.bison.netty.modules.oauth2.jwt.CustomJwtAccessTokenConverter;
import group.bison.netty.modules.oauth2.security.client.CustomJdbcClientDetailsService;
import group.bison.netty.modules.oauth2.security.code.CustomAuthorizationCodeServices;
import group.bison.netty.modules.oauth2.security.token.CustomAuthorizationTokenServices;
import group.bison.netty.modules.oauth2.security.token.CustomRedisTokenStore;
import group.bison.netty.modules.oauth2.security.token.CustomTokenStore;
import group.bison.netty.modules.oauth2.tools.TokenEncodeTool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.configuration.ObjectPostProcessorConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.net.UnknownHostException;
import java.util.Collections;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter implements InitializingBean {

    @Value("${spring.profiles.active}")
    private String env;

    private DataSource dataSource;

    private RedisConnectionFactory redisConnectionFactory;

    public OAuth2Config(DataSource dataSource, RedisConnectionFactory redisConnectionFactory) {
        this.dataSource = dataSource;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory, @Qualifier("dataSource") DataSource dataSource) {
        CustomTokenStore customTokenStore = new CustomTokenStore();
        customTokenStore.setCustomRedisTokenStore(new CustomRedisTokenStore(redisConnectionFactory));
        customTokenStore.setJdbcTokenStore(new JdbcTokenStore(dataSource));
        return customTokenStore;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean
    public JdbcClientDetailsService clientDetailsService() throws Exception {
        CustomJdbcClientDetailsService customJdbcClientDetailsService = new CustomJdbcClientDetailsService(dataSource);
        customJdbcClientDetailsService.setPasswordEncoder(passwordEncoder());
        return customJdbcClientDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(JdbcClientDetailsService clientDetailsService, PasswordEncoder passwordEncoder) {
        ClientDetailsUserDetailsService clientDetailsUserDetailsService = new ClientDetailsUserDetailsService(clientDetailsService);

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(clientDetailsUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(Collections.singletonList(daoAuthenticationProvider));
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager(clientDetailsService(), passwordEncoder()))
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(tokenStore(redisConnectionFactory, dataSource))
                .authorizationCodeServices(authorizationCodeServices())
                .tokenServices(authorizationServerTokenServices())
                .accessTokenConverter(accessTokenConverter())
                .exceptionTranslator(webResponseExceptionTranslator());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(IotServiceJwtHelper.SECRET);
//        return converter;
        return new CustomJwtAccessTokenConverter();
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        CustomAuthorizationCodeServices customAuthorizationCodeServices = new CustomAuthorizationCodeServices(redisConnectionFactory);
        return customAuthorizationCodeServices;
    }

    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() throws Exception {
        CustomAuthorizationTokenServices customTokenServices = new CustomAuthorizationTokenServices();
        customTokenServices.setTokenStore(tokenStore(redisConnectionFactory, dataSource));
        customTokenServices.setSupportRefreshToken(true);
        customTokenServices.setReuseRefreshToken(false);
        customTokenServices.setClientDetailsService(clientDetailsService());
        customTokenServices.setTokenEnhancer(accessTokenConverter());
        return customTokenServices;
    }

    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TokenEncodeTool.ENV = env;
    }

    @Configuration
    protected static class RedisConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = "redisTemplate")
        public RedisTemplate<Object, Object> redisTemplate(
                RedisConnectionFactory redisConnectionFactory)
                throws UnknownHostException {
            RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        }

        @Bean
        @ConditionalOnMissingBean(StringRedisTemplate.class)
        public StringRedisTemplate stringRedisTemplate(
                RedisConnectionFactory redisConnectionFactory)
                throws UnknownHostException {
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        }
    }
}

