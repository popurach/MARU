package com.bird.maru.common.handler;

import com.bird.maru.common.exception.NotEnoughMoneyException;
import com.bird.maru.common.exception.ResourceConflictException;
import com.bird.maru.common.exception.ResourceNotFoundException;
import javax.naming.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.UnprocessableEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final HttpHeaders headers;

    static {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler({ IllegalArgumentException.class, NotEnoughMoneyException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadRequest(Exception e) {
        return responseError(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AuthenticationException.class, JwtException.class })
    public ResponseEntity<String> handleUnAuthentication(Exception e) {
        return responseError(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<String> handleAuthorization(Exception e) {
        return responseError(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<String> handleNotFound(Exception e) {
        return responseError(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ResourceConflictException.class, DataIntegrityViolationException.class })
    public ResponseEntity<String> handleConflict(Exception e) {
        return responseError(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ UnprocessableEntity.class })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> handleUnprocessableParams(Exception e) {
        return responseError(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({ RuntimeException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        return responseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> responseError(String message, HttpStatus status) {
        return new ResponseEntity<>(message, headers, status);
    }

}
