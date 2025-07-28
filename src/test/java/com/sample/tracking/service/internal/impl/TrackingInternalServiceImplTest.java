package com.sample.tracking.service.internal.impl;

import com.sample.tracking.model.dto.common.CustomerInfo;
import com.sample.tracking.model.dto.request.TrackingParam;
import com.sample.tracking.model.dto.response.TrackingResponse;
import com.sample.tracking.model.entity.Tracking;
import com.sample.tracking.service.delegate.TrackingDelegateService;
import com.sample.tracking.util.TrackingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackingInternalServiceImplTest {

    @InjectMocks
    private TrackingInternalServiceImpl trackingInternalService;

    @Mock
    private TrackingDelegateService trackingDelegateService;

    private TrackingParam trackingParam;
    private Tracking trackingEntity;
    private CustomerInfo customerInfo;
    private TrackingResponse trackingResponse;
    private String trxNumber;

    @BeforeEach
    void setUp() {
        var now = LocalDateTime.now();
        trxNumber = TrackingUtil.generateTrackingNumber("ID", now);

        trackingParam = TrackingParam.builder()
                .originCountryId("ID")
                .destinationCountryId("US")
                .weight(BigDecimal.valueOf(10.5))
                .createdAt(now)
                .customerId("CUST001")
                .customerName("John Doe")
                .customerSlug("john-doe")
                .build();

        customerInfo = CustomerInfo.builder()
                .customerId("CUST001")
                .customerName("John Doe")
                .customerSlug("john-doe")
                .build();

        trackingEntity = Tracking.builder()
                .trackingNumber(trxNumber)
                .originCountryId("ID")
                .destinationCountryId("US")
                .weight(BigDecimal.valueOf(10.5))
                .createdAt(now)
                .customerInfo(customerInfo)
                .build();

        trackingResponse = TrackingResponse.builder()
                .trackingNumber(trxNumber)
                .createdAt(now)
                .customerId("CUST001")
                .customerName("John Doe")
                .customerSlug("john-doe")
                .originCountryId("ID")
                .destinationCountryId("US")
                .build();
    }

    @Test
    @DisplayName("Should successfully generate and save tracking number on first attempt")
    void fetchNextTrackingNumber_success() {
        try (MockedStatic<TrackingUtil> mockedTrackingUtil = Mockito.mockStatic(TrackingUtil.class)) {

            mockedTrackingUtil.when(() -> TrackingUtil.generateTrackingNumber(any(String.class), any(LocalDateTime.class)))
                    .thenReturn(trxNumber);

            when(trackingDelegateService.fetchByTrackingNumber(trxNumber))
                    .thenReturn(Mono.empty());

            mockedTrackingUtil.when(() -> TrackingUtil.generateTracking(any(String.class), any(TrackingParam.class)))
                    .thenReturn(trackingEntity);

            when(trackingDelegateService.saveTracking(any(Tracking.class)))
                    .thenReturn(Mono.just(trackingEntity));

            mockedTrackingUtil.when(() -> TrackingUtil.generateResponse(any(Tracking.class)))
                    .thenReturn(trackingResponse);

            Mono<TrackingResponse> resultMono = trackingInternalService.fetchNextTrackingNumber(trackingParam);

            StepVerifier.create(resultMono)
                    .expectNext(trackingResponse)
                    .verifyComplete();

            mockedTrackingUtil.verify(() -> TrackingUtil.generateTrackingNumber(any(String.class), any(LocalDateTime.class)), times(1));
            verify(trackingDelegateService, times(1)).fetchByTrackingNumber(trxNumber);
            mockedTrackingUtil.verify(() -> TrackingUtil.generateTracking(any(String.class), any(TrackingParam.class)), times(1));
            verify(trackingDelegateService, times(1)).saveTracking(any(Tracking.class));
            mockedTrackingUtil.verify(() -> TrackingUtil.generateResponse(any(Tracking.class)), times(1));
        }
    }

    @Test
    @DisplayName("Should generate a new tracking number after a collision and then save successfully")
    void fetchNextTrackingNumber_withCollision() {
        var now = LocalDateTime.now();
        var trxNumberNew = TrackingUtil.generateTrackingNumber("ID", now);

        try (MockedStatic<TrackingUtil> mockedTrackingUtil = Mockito.mockStatic(TrackingUtil.class)) {

            mockedTrackingUtil.when(() -> TrackingUtil.generateTrackingNumber(any(String.class), any(LocalDateTime.class)))
                    .thenReturn(trxNumber)
                    .thenReturn(trxNumberNew);

            when(trackingDelegateService.fetchByTrackingNumber(trxNumber))
                    .thenReturn(Mono.just(trackingEntity));

            when(trackingDelegateService.fetchByTrackingNumber(trxNumberNew))
                    .thenReturn(Mono.empty());

            mockedTrackingUtil.when(() -> TrackingUtil.generateTracking(any(String.class), any(TrackingParam.class)))
                    .thenReturn(trackingEntity);

            when(trackingDelegateService.saveTracking(any(Tracking.class)))
                    .thenReturn(Mono.just(trackingEntity));

            mockedTrackingUtil.when(() -> TrackingUtil.generateResponse(any(Tracking.class)))
                    .thenReturn(trackingResponse);

            Mono<TrackingResponse> resultMono = trackingInternalService.fetchNextTrackingNumber(trackingParam);

            StepVerifier.create(resultMono)
                    .expectNext(trackingResponse)
                    .verifyComplete();

            mockedTrackingUtil.verify(() -> TrackingUtil.generateTrackingNumber(any(String.class), any(LocalDateTime.class)), times(2));
            verify(trackingDelegateService, times(1)).fetchByTrackingNumber(trxNumber);
            verify(trackingDelegateService, times(1)).fetchByTrackingNumber(trxNumberNew);
            mockedTrackingUtil.verify(() -> TrackingUtil.generateTracking(any(String.class), any(TrackingParam.class)), times(1));
            verify(trackingDelegateService, times(1)).saveTracking(any(Tracking.class));
            mockedTrackingUtil.verify(() -> TrackingUtil.generateResponse(any(Tracking.class)), times(1));
        }
    }

    @Test
    @DisplayName("Should propagate error when saveTracking fails")
    void fetchNextTrackingNumber_saveFails() {
        try (MockedStatic<TrackingUtil> mockedTrackingUtil = Mockito.mockStatic(TrackingUtil.class)) {

            mockedTrackingUtil.when(() -> TrackingUtil.generateTrackingNumber(any(String.class), any(LocalDateTime.class)))
                    .thenReturn(trxNumber);

            when(trackingDelegateService.fetchByTrackingNumber(trxNumber))
                    .thenReturn(Mono.empty());

            mockedTrackingUtil.when(() -> TrackingUtil.generateTracking(any(String.class), any(TrackingParam.class)))
                    .thenReturn(trackingEntity);

            RuntimeException expectedException = new RuntimeException("Database save failed");
            when(trackingDelegateService.saveTracking(any(Tracking.class)))
                    .thenReturn(Mono.error(expectedException));


            Mono<TrackingResponse> resultMono = trackingInternalService.fetchNextTrackingNumber(trackingParam);

            StepVerifier.create(resultMono)
                    .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                            throwable.getMessage().equals(expectedException.getMessage()))
                    .verify();

            mockedTrackingUtil.verify(() -> TrackingUtil.generateTrackingNumber(any(String.class), any(LocalDateTime.class)), times(1));
            verify(trackingDelegateService, times(1)).fetchByTrackingNumber(trxNumber);
            mockedTrackingUtil.verify(() -> TrackingUtil.generateTracking(any(String.class), any(TrackingParam.class)), times(1));
            verify(trackingDelegateService, times(1)).saveTracking(any(Tracking.class));
            mockedTrackingUtil.verify(() -> TrackingUtil.generateResponse(any(Tracking.class)), times(0));
        }
    }
}
