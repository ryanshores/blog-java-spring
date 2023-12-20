package com.ryanshores.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    private static final String[] WHITELIST = { "/", "/register", "/h2-console/*", "/api/auth" };
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";
    private static final String LOGOUT_SUCCESS_URL = LOGIN_URL + "?logout";
    private static final String LOGIN_FAIL_URL = LOGIN_URL + "?error";
    private static final String POST_URL = "/post/**";
    private static final String API_URL = "/api/**";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String USERNAME = "email";
    private static final String PASSWORD = "password";
    private static final String COOKIE = "JSESSIONID";


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(WHITELIST).permitAll()
                .requestMatchers(HttpMethod.GET,POST_URL).permitAll()
                .requestMatchers(API_URL).permitAll()
                .anyRequest()
                .authenticated()
            )
            .formLogin(opts -> opts
                .loginPage(LOGIN_URL)
                .loginProcessingUrl(LOGIN_URL)
                .usernameParameter(USERNAME)
                .passwordParameter(PASSWORD)
                .defaultSuccessUrl("/", true)
                .failureUrl(LOGIN_FAIL_URL)
                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl(LOGOUT_URL)
                    .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                    .invalidateHttpSession(true)
                    .deleteCookies(COOKIE)
            )
            .httpBasic(Customizer.withDefaults());

        // TODO: when moving away from h2 console this can be removed
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}
