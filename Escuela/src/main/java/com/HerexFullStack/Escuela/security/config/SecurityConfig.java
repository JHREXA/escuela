package com.HerexFullStack.Escuela.security.config;

import com.HerexFullStack.Escuela.security.config.filters.JwtTokenValidator;
import com.HerexFullStack.Escuela.service.UserDetailsServiceImp;
import com.HerexFullStack.Escuela.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


/**
 * Spring Security configuration class.
 *
 * Responsible for configuring authentication,
 * authorization, session management and password encoding.
 *
 * Main responsibilities:
 *
 * - Configure SecurityFilterChain
 * - Configure AuthenticationManager
 * - Register AuthenticationProvider
 * - Configure password encryption
 * - Define stateless session strategy
 *
 * Security strategy:
 *
 * - JWT based authentication
 * - Stateless sessions
 * - BCrypt password encryption
 * - Custom UserDetailsService
 *
 * Since JWT is used, sessions are not stored
 * on the server side.
 *
 * @author Jesus Hernandez Rexa
 * @since 1.0
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Configures Spring Security filter chain.
     *
     * Security configuration:
     *
     * - Disables CSRF protection
     * - Enables HTTP Basic authentication
     * - Uses stateless session strategy
     * - Allows form login
     * - Registers custom JWT filter
     *
     * JWT filter execution:
     *
     * JwtTokenValidator is executed before
     * BasicAuthenticationFilter.
     *
     * This allows incoming JWT tokens to be:
     *
     * - extracted from Authorization header
     * - validated
     * - converted into Authentication objects
     * - stored inside SecurityContext
     *
     * Once authentication exists in SecurityContext,
     * Spring Security treats the request as authenticated.
     *
     * @param httpSecurity HttpSecurity configuration
     * @param authenticationProvider authentication provider
     * @return configured SecurityFilterChain
     * @throws Exception configuration exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthenticationProvider authenticationProvider)throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.permitAll())
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    /**
     * Creates AuthenticationManager bean.
     *
     * AuthenticationManager is responsible for processing
     * authentication requests and delegating authentication
     * logic to the configured AuthenticationProvider.
     *
     * @param authenticationConfiguration authentication configuration
     *
     * @return configured AuthenticationManager
     *
     * @throws Exception configuration exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures DaoAuthenticationProvider.
     *
     * DaoAuthenticationProvider retrieves users from
     * UserDetailsService and validates passwords
     * using the configured PasswordEncoder.
     *
     * @param userDetailsService custom user service
     *
     * @return configured AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImp userDetailsService){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /**
     * Password encoder bean.
     *
     * BCrypt automatically hashes passwords and
     * adds salt protection.
     *
     * Example:
     *
     * raw password:
     * admin123
     *
     * encrypted:
     * $2a$10$......
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}


