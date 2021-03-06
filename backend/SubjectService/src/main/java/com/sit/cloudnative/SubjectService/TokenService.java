package com.sit.cloudnative.SubjectService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sit.cloudnative.SubjectService.exception.BadRequestException;
import com.sit.cloudnative.SubjectService.exception.UnauthorizedException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    
    @Value("${token.maxAgeSeconds}")
    private long maxAgeSeconds;
    
    @Value("${token.secret}") 
    private String secret;
    
    private Algorithm algorithm;
    
    private JWTVerifier verifier;
    
    public TokenService() {
    }
    
    @PostConstruct
    private void init(){
        algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm).build();
    }
    
    public DecodedJWT checkToken(String token) throws JWTVerificationException{
        DecodedJWT djwt = verifier.verify(token);
        return djwt;
    }
    
    public String getUser(String token){
        DecodedJWT djwt = checkToken(token);
        Claim claim = djwt.getClaim("username");
        return claim.asString();
    }

    public void validateToken (String auth, HttpServletRequest request, Logger logger) {
        if(auth.trim().isEmpty()){
            logger.warn(System.currentTimeMillis() + " | " + request.getRemoteAddr() + " | " + "Authorization token not found in header");
            throw new BadRequestException("Not have value in Authorization");
        }
        try {
            checkToken(auth);
        } catch (AlgorithmMismatchException e) {
            logger.warn(System.currentTimeMillis() + " | " + request.getRemoteAddr() + " | " + "not match token algorithm (" + auth + ")");
            throw new UnauthorizedException(e.getMessage());
        } catch (SignatureVerificationException e) {
            logger.warn(System.currentTimeMillis() + " | " + request.getRemoteAddr() + " | " + "secret key is not valid (" + auth + ")");
            throw new UnauthorizedException(e.getMessage());
        } catch (TokenExpiredException e) {
            logger.warn(System.currentTimeMillis() + " | " + request.getRemoteAddr() + " | " + "token is expired (" + auth + ")");
            throw new UnauthorizedException(e.getMessage());
        } catch (InvalidClaimException e) {
            logger.warn(System.currentTimeMillis() + " | " + request.getRemoteAddr() + " | " + "invalid claim value (" + auth + ")");
            throw new UnauthorizedException(e.getMessage());
        } catch (JWTDecodeException e) {
            logger.warn(System.currentTimeMillis() + " | " + request.getRemoteAddr() + " | " + "token does not contain 3 parts (" + auth + ")");
            throw new UnauthorizedException(e.getMessage());
        } catch (Exception e) {
            logger.warn(System.currentTimeMillis() + " | " + request.getRemoteAddr() + " | " + "unknown error (" + auth + ")");
        }
    }
}
