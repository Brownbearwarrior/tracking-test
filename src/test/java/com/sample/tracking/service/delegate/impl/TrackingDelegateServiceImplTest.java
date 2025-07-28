package com.sample.tracking.service.delegate.impl;

import com.sample.tracking.model.dto.common.CustomerInfo;
import com.sample.tracking.model.entity.Tracking;
import com.sample.tracking.repository.TrackingRepository;
import com.sample.tracking.util.TrackingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackingDelegateServiceImplTest {

    @InjectMocks
    private TrackingDelegateServiceImpl trackingDelegateService;

    @Mock
    private TrackingRepository trackingRepository;

    private Tracking trackingEntity;
    private CustomerInfo customerInfo;
    private String trackingNumber;

    @BeforeEach
    void setUp() {
        var now = LocalDateTime.now();
        trackingNumber = TrackingUtil.generateTrackingNumber("ID", now);

        customerInfo = CustomerInfo.builder()
                .customerId("CUST001")
                .customerName("John Doe")
                .customerSlug("john-doe")
                .build();

        trackingEntity = Tracking.builder()
                .trackingNumber(trackingNumber)
                .originCountryId("ID")
                .destinationCountryId("US")
                .weight(BigDecimal.valueOf(10.5))
                .createdAt(now)
                .customerInfo(customerInfo)
                .build();
    }

    @Test
    @DisplayName("Should successfully save a tracking entity")
    void saveTracking_success() {
        when(trackingRepository.save(any(Tracking.class)))
                .thenReturn(Mono.just(trackingEntity));

        Mono<Tracking> resultMono = trackingDelegateService.saveTracking(trackingEntity);

        StepVerifier.create(resultMono)
                .expectNext(trackingEntity)
                .verifyComplete();

        verify(trackingRepository, times(1)).save(trackingEntity);
    }

    @Test
    @DisplayName("Should propagate error when saving tracking entity fails")
    void saveTracking_error() {
        RuntimeException expectedException = new RuntimeException("Database save error");
        when(trackingRepository.save(any(Tracking.class)))
                .thenReturn(Mono.error(expectedException));

        Mono<Tracking> resultMono = trackingDelegateService.saveTracking(trackingEntity);

        StepVerifier.create(resultMono)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(expectedException.getMessage()))
                .verify();

        verify(trackingRepository, times(1)).save(trackingEntity);
    }

    @Test
    @DisplayName("Should successfully fetch a tracking entity by tracking number")
    void fetchByTrackingNumber_found() {
        when(trackingRepository.findById(trackingNumber))
                .thenReturn(Mono.just(trackingEntity));

        Mono<Tracking> resultMono = trackingDelegateService.fetchByTrackingNumber(trackingNumber);

        StepVerifier.create(resultMono)
                .expectNext(trackingEntity)
                .verifyComplete();

        verify(trackingRepository, times(1)).findById(trackingNumber);
    }

    @Test
    @DisplayName("Should return empty Mono when tracking number is not found")
    void fetchByTrackingNumber_notFound() {
        when(trackingRepository.findById(trackingNumber))
                .thenReturn(Mono.empty());

        Mono<Tracking> resultMono = trackingDelegateService.fetchByTrackingNumber(trackingNumber);

        StepVerifier.create(resultMono)
                .expectNextCount(0)
                .verifyComplete();

        verify(trackingRepository, times(1)).findById(trackingNumber);
    }

    @Test
    @DisplayName("Should propagate error when fetching by tracking number fails")
    void fetchByTrackingNumber_error() {
        RuntimeException expectedException = new RuntimeException("Database fetch error");
        when(trackingRepository.findById(trackingNumber))
                .thenReturn(Mono.error(expectedException));

        Mono<Tracking> resultMono = trackingDelegateService.fetchByTrackingNumber(trackingNumber);

        StepVerifier.create(resultMono)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(expectedException.getMessage()))
                .verify();

        verify(trackingRepository, times(1)).findById(trackingNumber);
    }
}
