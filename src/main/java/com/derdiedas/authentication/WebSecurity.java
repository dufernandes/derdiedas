package com.derdiedas.authentication;

import com.derdiedas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.derdiedas.authentication.SecurityConstants.STATIC_DOCS_URL_PATTERN;
import static com.derdiedas.authentication.SecurityConstants.SIGN_UP_URL_PATTERN;

/**
 * Class responsible for authentication and authorization.
 * <br>
 * By extending {@link WebSecurityConfigurerAdapter}, we take advantage of
 * Spring default web security configuration - https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-security.html.
 */
@Configuration
@EnableWebSecurity
// this bean is only instantiated when the profile nosecure
// is used. The nosecure profile is used to launch the application
// without security
@Profile("!nosecure")
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurity(UserService userService,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL_PATTERN).permitAll()
                // allow static docs
                .antMatchers(STATIC_DOCS_URL_PATTERN).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * This method is overridden so it can be configured the implementation of
     * {@link org.springframework.security.core.userdetails.UserDetailsService} we use,
     * along with the {@link org.springframework.security.crypto.password.PasswordEncoder }
     * in place.
     * <br>
     * Based on these configurations, Spring can, for instance login a user.
     *
     * @param auth Authentication Manager builder.
     * @throws Exception In case any problem occurs.
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * For now e allow CORS from everywhere.
     *
     * @return CORS configuration object.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
