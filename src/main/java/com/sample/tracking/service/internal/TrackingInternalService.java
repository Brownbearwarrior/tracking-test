package com.sample.tracking.service.internal;

import com.sample.tracking.model.dto.request.TrackingParam;
import com.sample.tracking.model.dto.response.TrackingResponse;
import reactor.core.publisher.Mono;

public interface TrackingInternalService {
    Mono<TrackingResponse> fetchNextTrackingNumber(TrackingParam trackingParam);
}
