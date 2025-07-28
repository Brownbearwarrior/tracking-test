package com.sample.tracking.service.internal.impl;

import com.sample.tracking.model.dto.request.TrackingParam;
import com.sample.tracking.model.dto.response.TrackingResponse;
import com.sample.tracking.service.internal.TrackingInternalService;
import com.sample.tracking.service.delegate.TrackingDelegateService;
import com.sample.tracking.util.TrackingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TrackingInternalServiceImpl implements TrackingInternalService {

    private final TrackingDelegateService trackingDelegateService;

    @Override
    public Mono<TrackingResponse> fetchNextTrackingNumber(TrackingParam trackingParam) {
        return generateTrackingNumberValid(trackingParam)
                .flatMap(trxNumber -> {
                    var tracking = TrackingUtil.generateTracking(trxNumber, trackingParam);

                    return trackingDelegateService.saveTracking(tracking);
                })
                .map(TrackingUtil::generateResponse);
    }

    private Mono<String> generateTrackingNumberValid(TrackingParam trackingParam){
        return Mono.just(trackingParam)
                .map(param -> TrackingUtil.generateTrackingNumber(param.getOriginCountryId(), param.getCreatedAt()))
                .flatMap(trxNumber ->
                        trackingDelegateService.fetchByTrackingNumber(trxNumber)
                                .flatMap(existingTracking -> generateTrackingNumberValid(trackingParam))
                                .switchIfEmpty(Mono.just(trxNumber))
                );
    }
}
