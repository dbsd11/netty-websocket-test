package group.bison.netty.modules.oauth2.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.configuration.ObjectPostProcessorConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Configuration
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public ObjectPostProcessorConfiguration objectPostProcessorConfiguration() {
        return new ObjectPostProcessorConfiguration();
    } 

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        HeaderWriter headerWriter = (request, response) -> {
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type,Authorization,X-Amz-Date,X-Api-Key,X-Amz-Security-Token");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "DELETE,GET,HEAD,OPTIONS,PATCH,POST,PUT");
        };

        // pass option request
        http.addFilter(new ChannelProcessingFilter() {
            @Override
            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
                if (req instanceof HttpServletRequest && HttpMethod.OPTIONS.name().equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
                    ((HttpServletResponse) res).setStatus(HttpStatus.OK.value());
                    headerWriter.writeHeaders((HttpServletRequest) req, (HttpServletResponse) res);
                } else {
                    chain.doFilter(req, res);
                }
            }
        });

        // cors support
        http.addFilter(new HeaderWriterFilter(Collections.singletonList(headerWriter)));

        // 配置服务那些url需要验证, 哪些不需要. Oauth endpoint需配置为permitAll， 否则会出现套娃ProviderManager.authenticate -> AuthenticationManager.authenticate -> ProviderManager.authenticate -> ...
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and().requestMatchers().antMatchers("/**")
                .and().authorizeRequests().antMatchers("/oauth/**", "/actuator/**").permitAll().anyRequest().authenticated()
                .and().httpBasic();

        http.setSharedObject(SecurityContextRepository.class, new HttpSessionSecurityContextRepository() {
            @Override
            public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
                SecurityContext securityContext = super.loadContext(requestResponseHolder);
                if (securityContext != null) {
                    Authentication authentication = tryCreateAuthentication(requestResponseHolder.getRequest());
                    securityContext.setAuthentication(authentication != null ? authentication : securityContext.getAuthentication());
                }
                return securityContext;
            }

            @Override
            public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
                super.saveContext(context, request, response);
            }
        });
    }

    Authentication tryCreateAuthentication(HttpServletRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", Collections.emptyList());
        return authentication;
    }
}
