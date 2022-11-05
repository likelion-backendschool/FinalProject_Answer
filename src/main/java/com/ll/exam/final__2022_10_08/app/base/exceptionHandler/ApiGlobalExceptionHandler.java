package com.ll.exam.final__2022_10_08.app.base.exceptionHandler;

import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.util.Ut;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice(annotations = {RestController.class}) // 모든 @RestController 에서 발생한 예외에 대한 제어권을 가로챈다.
public class ApiGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RsData<String>> errorHandler(MethodArgumentNotValidException exception) {
        String msg = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("/"));

        String data = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getCode)
                .collect(Collectors.joining("/"));

        return Ut.sp.responseEntityOf(RsData.of("F-MethodArgumentNotValidException", msg, data));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RsData<String>> errorHandler(RuntimeException exception) {
        String msg = exception.getClass().toString();

        String data = exception.getMessage();

        return Ut.sp.responseEntityOf(RsData.of("F-RuntimeException", msg, data));
    }
}
