package com.HerexFullStack.Escuela.security.config.filters;

import com.HerexFullStack.Escuela.security.config.utils.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

/**
 * JWT authentication filter executed once per request.
 *
 * This filter intercepts incoming HTTP requests and checks
 * whether an Authorization header containing a JWT token exists.
 *
 * Authentication flow:
 *
 * 1. Read Authorization header
 * 2. Extract JWT token
 * 3. Validate token integrity and expiration
 * 4. Extract username and authorities
 * 5. Convert authorities into GrantedAuthority objects
 * 6. Create Authentication object
 * 7. Store authentication in SecurityContext
 *
 * Once authentication is stored in SecurityContextHolder,
 * Spring Security recognizes the user as authenticated
 * for the current request.
 *
 * Example header:
 *
 * Authorization: Bearer eyJhbGciOiJIUzI1Ni...
 *
 * @author Jesús Hernández Rexa
 * @since 1.0
 */
public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    /**
     * Constructor that injects JWT utility methods.
     *
     * @param jwtUtils utility component responsible for
     *                 token validation and claim extraction
     */
    public JwtTokenValidator(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    /**
     * Performs JWT validation for every incoming request.
     *
     * This method:
     *
     * - Reads Authorization header
     * - Removes Bearer prefix
     * - Validates token
     * - Extracts username
     * - Extracts authorities
     * - Creates Authentication object
     * - Stores authentication inside SecurityContext
     *
     * If no token exists, the request continues without authentication.
     *
     * @param request incoming HTTP request
     * @param response outgoing HTTP response
     * @param filterChain Spring Security filter chain
     *
     * @throws ServletException servlet exception during processing
     * @throws IOException input/output exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(jwtToken != null && jwtToken.startsWith("Bearer ")){
            jwtToken = jwtToken.substring(7);
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

            String username = jwtUtils.extractUsername(decodedJWT);
            String authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

            Collection<? extends GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,authoritiesList);

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request,response);
    }
}
