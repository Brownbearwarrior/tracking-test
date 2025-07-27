package com.sample.tracking.config.handler;

import com.sample.tracking.config.exception.LogicException;
import com.sample.tracking.model.constant.Message;
import com.sample.tracking.model.dto.common.Response;
import com.sample.tracking.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Mono<Response<Object>> methodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        return Mono.defer(() -> Mono.just(ResponseUtil.generateResponse(Message.METHOD_ARGUMENTS_NOT_VALID, null)));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Mono<Response<Object>> bindException(BindException bindException){
        return Mono.defer(() ->  Mono.just(ResponseUtil.generateResponse(Message.BIND_ERROR, null)));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Mono<Response<Object>> exception(Exception exception){
        return Mono.just(ResponseUtil.generateResponse(Message.SYSTEM_ERROR, null));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Mono<Response<Object>> runtimeException(RuntimeException runtimeException){
        return Mono.just(ResponseUtil.generateResponse(Message.RUNTIME_ERROR, null));
    }

    @ExceptionHandler(LogicException.class)
    Mono<Response<Object>> logicException(LogicException logicException){
        return Mono.just(ResponseUtil.generateResponse(logicException.getMessage(), null));
    }
}
