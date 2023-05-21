package group.bison.netty.modules.oauth2.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import group.bison.netty.modules.oauth2.jwt.JwtHelper;

@Configuration
public class ReactiveServerSecurityConfig {
    
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user1 = User.builder().passwordEncoder(encoder::encode).username("admin").password("passwd")
                .roles("USER", "ADMIN", "CLIENT")
                .authorities("SCOPE_resource.read").build();

        UserDetails user2 = User.builder().passwordEncoder(encoder::encode).username("user").password("nopasswd")
                .roles("USER").build();
        return new MapReactiveUserDetailsService(user1, user2);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .cors().configurationSource((exchange) -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList("*"));
                    configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization","X-Amz-Date","X-Api-Key","X-Amz-Security-Token"));
                    configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"));
                    return configuration;
                }).and()
                .csrf().disable()
                .authorizeExchange()
                    .anyExchange().authenticated().and()
                    .httpBasic().and()
                    .formLogin().and()
                    .oauth2ResourceServer().jwt((jwtConf) -> {
                        jwtConf.jwtDecoder(JwtHelper.getInstance());
                    }).bearerTokenConverter(new ServerBearerTokenAuthenticationConverter() {{
                        setAllowUriQueryParameter(true);
                    }}).and()
                .build();
    }
}
