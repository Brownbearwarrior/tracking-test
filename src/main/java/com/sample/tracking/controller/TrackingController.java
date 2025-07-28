package com.sample.tracking.controller;

import com.sample.tracking.model.constant.Message;
import com.sample.tracking.model.constant.Path;
import com.sample.tracking.model.dto.common.Response;
import com.sample.tracking.model.dto.response.TrackingResponse;
import com.sample.tracking.service.internal.TrackingInternalService;
import com.sample.tracking.util.ResponseUtil;
import com.sample.tracking.util.TrackingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TrackingController {
    private final TrackingInternalService trackingInternalService;

    @GetMapping(Path.TEST)
    Mono<Response<String>> fetchNextTrackingNumber(){
        return Mono.just(ResponseUtil.generateResponse(Message.SUCCESS, "this is sample test"));
    }

    @GetMapping(Path.TRACKING_NUMBER)
    Mono<Response<TrackingResponse>> fetchNextTrackingNumber(@RequestParam Map<String, String> params){
        return Mono.just(TrackingUtil.generateTrackingParam(params))
                .flatMap(trackingInternalService::fetchNextTrackingNumber)
                .map(response -> ResponseUtil.generateResponse(Message.SUCCESS, response));
    }
}
