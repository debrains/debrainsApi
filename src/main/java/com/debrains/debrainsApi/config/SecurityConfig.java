package com.debrains.debrainsApi.config;

import com.debrains.debrainsApi.security.jwt.JwtAccessDeniedHandler;
import com.debrains.debrainsApi.security.jwt.JwtAuthenticationEntryPoint;
import com.debrains.debrainsApi.security.jwt.JwtAuthenticationFilter;
import com.debrains.debrainsApi.security.oauth.CookieAuthorizationRequestRepository;
import com.debrains.debrainsApi.security.oauth.CustomOAuth2UserService;
import com.debrains.debrainsApi.security.oauth.OAuth2AuthenticationFailureHandler;
import com.debrains.debrainsApi.security.oauth.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Log4j2
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth2/**", "/auth/**").permitAll()
                .antMatchers("/root/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.cors()                     // CORS on
            .and()
            .csrf().disable()           // CSRF off
            .httpBasic().disable()      // Basic Auth off
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);    // Session off

        http.formLogin().disable()
            .oauth2Login()
                .authorizationEndpoint()
                    .baseUri("/oauth2/authorization")
                    .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                    .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                .and()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
