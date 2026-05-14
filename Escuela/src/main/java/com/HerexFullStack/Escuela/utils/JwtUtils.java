package com.HerexFullStack.Escuela.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


/*
* Utility component responsible for creating, validating and reading JWT tokens.
* */
@Component
public class JwtUtils {

    @Value("${security.jwt.private.key}")
    private String privateKey;
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    /*
    * Creates a JWT from the authenticated user.
    * @param authentication Spring Security authentication object.
    * @return signed JWT token
    * */
    public String createToken(Authentication autentication){

        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        String username = autentication.getPrincipal().toString();

        String authorities = autentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 18000000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);

            return jwtToken;
    }

    /*
    * Validates a JWT token and returns a decoded token.
    * @param token JWT token
    * @return decoded JWT token
    * @throws JWTVerificationException if the token is expired or invalid
    * */
    public DecodedJWT validateToken(String token){

        try{
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        }catch (JWTVerificationException exception){
            throw new JWTVerificationException("Invalid token. Not authorized.");
        }
    }

    /*
    * Extracts the username from a decoded JWT.
    * @param decodedJWT decodedJWT
    * @return username stored in the subject.
    *
    * */
    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    /*
    * Gets a specific claim from the decoded JWT
    * @param decodedJWT decoded JWT
    * @param claimName name of the claim
    * @return claim value
    * */
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    /*
    * Gets all claims from a decoded JWT
    * @param decodedJWT decoded JWT
    * @return map with all the claims and their names.
    * */
    public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}
