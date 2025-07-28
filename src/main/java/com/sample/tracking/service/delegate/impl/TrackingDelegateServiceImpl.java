package com.sample.tracking.service.delegate.impl;

import com.sample.tracking.model.entity.Tracking;
import com.sample.tracking.repository.TrackingRepository;
import com.sample.tracking.service.delegate.TrackingDelegateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TrackingDelegateServiceImpl implements TrackingDelegateService {

    private final TrackingRepository trackingRepository;

    @Override
    public Mono<Tracking> saveTracking(Tracking tracking) {
        return trackingRepository.save(tracking);
    }

    @Override
    public Mono<Tracking> fetchByTrackingNumber(String trackingNumber) {
        return trackingRepository.findById(trackingNumber);
    }
}
