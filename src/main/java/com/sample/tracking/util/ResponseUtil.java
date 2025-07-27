package com.sample.tracking.util;

import com.sample.tracking.model.dto.common.Response;

public class ResponseUtil {
    private ResponseUtil(){}

    public static <T> Response<T> generateResponse(String message, T data){
        return new Response<>(message, data);
    }
}
