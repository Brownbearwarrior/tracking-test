package com.sample.tracking.service.delegate;

import com.sample.tracking.model.entity.Tracking;
import reactor.core.publisher.Mono;

public interface TrackingDelegateService {
    Mono<Tracking> saveTracking(Tracking tracking);
    Mono<Tracking> fetchByTrackingNumber(String trackingNumber);
}
