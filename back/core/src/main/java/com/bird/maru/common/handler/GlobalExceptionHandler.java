package com.bird.maru.common.handler;

import com.bird.maru.common.exception.NotEnoughMoneyException;
import com.bird.maru.common.exception.ResourceConflictException;
import com.bird.maru.common.exception.ResourceNotFoundException;
import javax.naming.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.UnprocessableEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ IllegalArgumentException.class, NotEnoughMoneyException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({ AuthenticationException.class, JwtException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnAuthentication(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAuthorization(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({ ResourceConflictException.class, DataIntegrityViolationException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflict(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({ UnprocessableEntity.class })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleUnprocessableParams(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({ RuntimeException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(Exception e) {
        return e.getMessage();
    }

}
