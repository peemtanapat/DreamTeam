package com.sit.cloudnative.VideoService.Videos;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sit.cloudnative.VideoService.TokenService;
import com.sit.cloudnative.VideoService.exception.BadRequestException;
import com.sit.cloudnative.VideoService.exception.NotFoundException;
import com.sit.cloudnative.VideoService.exception.UnauthorizedException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class VideosController {

    @Autowired
    private VideosService videosService;
    
    @Autowired
    private TokenService tokenService;

    Logger logger = LoggerFactory.getLogger(VideosController.class);

    @GetMapping("/subject/{subjectId}/videos")
    public ResponseEntity<List<Videos>> getVideoList(@PathVariable long subjectId, 
                                                     @RequestHeader("Authorization") String auth,
                                                     HttpServletRequest request) {
        tokenService.validateToken(auth, request, logger);
        try {
            List<Videos> videoList = videosService.getVideoListBySubjectId(subjectId);
            return new ResponseEntity<>(videoList, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.warn(System.currentTimeMillis() + " | " + tokenService.getUser(auth) + " | " + "not found subject id (" + subjectId + ")");
            throw new NotFoundException("subject " + subjectId + " not found");
        }
    }

}
