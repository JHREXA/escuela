package com.HerexFullStack.Escuela.security.config;

import com.HerexFullStack.Escuela.security.config.filters.JwtTokenValidator;
import com.HerexFullStack.Escuela.service.UserDetailsServiceImp;
import com.HerexFullStack.Escuela.security.config.utils.JwtUtils;
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
 * Main Spring Security configuration class.
 *
 * This class defines the security behavior of the application,
 * including authentication, session management, JWT validation
 * and password encryption.
 *
 * Main responsibilities:
 *
 * - Configure the security filter chain
 * - Register the JWT validation filter
 * - Configure stateless session management
 * - Expose the AuthenticationManager bean
 * - Configure the AuthenticationProvider
 * - Configure BCrypt password encryption
 *
 * Security strategy:
 *
 * - JWT based authentication
 * - Stateless sessions
 * - Custom UserDetailsService implementation
 * - BCrypt password hashing
 *
 * Since JWT authentication is used, the server does not store
 * authentication sessions. Each request must provide its JWT token
 * through the Authorization header.
 *
 * Example Authorization header:
 *
 * Authorization: Bearer eyJhbGciOiJIUzI1Ni...
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
     * Configures the main Spring Security filter chain.
     *
     * This method defines how incoming HTTP requests are secured by the application.
     *
     * Current configuration:
     *
     * - Disables CSRF protection.
     * - Enables HTTP Basic authentication.
     * - Configures the application as stateless.
     * - Defines public and protected endpoints.
     * - Enables form login temporarily.
     * - Registers the custom AuthenticationProvider.
     * - Adds the custom JWT validation filter before BasicAuthenticationFilter.
     *
     * Authorization rules:
     *
     * - /auth/** is public and does not require authentication.
     * - Any other request requires authentication.
     *
     * JWT authentication flow:
     *
     * 1. JwtTokenValidator reads the Authorization header.
     * 2. If a Bearer token exists, the token is extracted.
     * 3. JwtUtils validates the token.
     * 4. Username and authorities are extracted from the token.
     * 5. An Authentication object is created.
     * 6. The Authentication object is stored in SecurityContext.
     * 7. The request continues through the filter chain.
     *
     * @param httpSecurity Spring Security HTTP configuration object
     * @param authenticationProvider custom authentication provider used by Spring Security
     * @return configured SecurityFilterChain
     * @throws Exception if the security filter chain cannot be built
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthenticationProvider authenticationProvider)throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())

                .httpBasic(Customizer.withDefaults())

                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.permitAll())

                .authenticationProvider(authenticationProvider)

                .addFilterBefore(
                        new JwtTokenValidator(jwtUtils),
                        BasicAuthenticationFilter.class)
                .build();
    }

    /**
     * Exposes the AuthenticationManager as a Spring bean.
     *
     * AuthenticationManager is responsible for processing authentication
     * requests. In the login flow, it receives the username and password,
     * delegates authentication to the configured AuthenticationProvider,
     * and returns an Authentication object if credentials are valid.
     *
     * This bean can be injected into services such as AuthService.
     *
     * @param authenticationConfiguration Spring authentication configuration
     * @return configured AuthenticationManager
     * @throws Exception if the AuthenticationManager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the authentication provider used by Spring Security.
     *
     * DaoAuthenticationProvider uses a UserDetailsService to load users
     * from the database and a PasswordEncoder to verify passwords.
     *
     * In this application:
     *
     * - UserDetailsServiceImp loads users, roles and permissions
     * - BCryptPasswordEncoder verifies encrypted passwords
     *
     * @param userDetailsService custom service that loads users from the database
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


