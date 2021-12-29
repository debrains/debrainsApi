package com.debrains.debrainsApi.exception;

import com.debrains.debrainsApi.common.exception.ExceptionType;
import com.debrains.debrainsApi.common.exception.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<Message> exceptionHandler(HttpServletRequest request, final ApiException e) {
        return ResponseEntity
                .status(e.getError().getStatus())
                .body(Message.builder()
                        .statusCode(e.getError().getCode())
                        .responseMessage(e.getError().getMessage())
                        .build());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Message> exceptionHandler(HttpServletRequest request, final RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ExceptionType.RUNTIME_EXCEPTION.getStatus())
                .body(Message.builder()
                        .statusCode(ExceptionType.RUNTIME_EXCEPTION.getCode())
                        .responseMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Message> exceptionHandler(HttpServletRequest request, final AccessDeniedException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ExceptionType.ACCESS_DENIED_EXCEPTION.getStatus())
                .body(Message.builder()
                        .statusCode(ExceptionType.ACCESS_DENIED_EXCEPTION.getCode())
                        .responseMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Message> exceptionHandler(HttpServletRequest request, final Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ExceptionType.INTERNAL_SERVER_ERROR.getStatus())
                .body(Message.builder()
                        .statusCode(ExceptionType.INTERNAL_SERVER_ERROR.getCode())
                        .responseMessage(e.getMessage())
                        .build());
    }

}
