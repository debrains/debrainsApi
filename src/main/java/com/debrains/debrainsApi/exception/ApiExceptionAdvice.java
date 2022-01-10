package com.debrains.debrainsApi.exception;

import com.debrains.debrainsApi.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ResponseDTO> exceptionHandler(HttpServletRequest request, final ApiException e) {
        return ResponseEntity
                .status(e.getResponse().getStatus())
                .body(ResponseDTO.builder()
                        .code(e.getResponse().getCode())
                        .message(e.getResponse().getMessage())
                        .build());
    }

}
