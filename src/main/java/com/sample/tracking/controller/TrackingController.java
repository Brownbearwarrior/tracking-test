package com.sample.tracking.controller;

import com.sample.tracking.model.constant.Message;
import com.sample.tracking.model.constant.Path;
import com.sample.tracking.model.dto.common.Response;
import com.sample.tracking.model.dto.request.TrackingParam;
import com.sample.tracking.model.dto.response.TrackingResponse;
import com.sample.tracking.service.internal.TrackingInternalService;
import com.sample.tracking.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingInternalService trackingInternalService;

    @GetMapping(Path.TRACKING_NUMBER)
    Mono<Response<TrackingResponse>> fetchNextTrackingNumber(TrackingParam trackingParam){
        return trackingInternalService.fetchNextTrackingNumber(trackingParam)
                .map(response -> ResponseUtil.generateResponse(Message.MESSAGE_SUCCESS, response));
    }
}
