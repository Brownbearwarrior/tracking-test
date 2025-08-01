package com.sample.tracking.controller;

import com.sample.tracking.model.constant.Message;
import com.sample.tracking.model.constant.Path;
import com.sample.tracking.model.dto.common.Response;
import com.sample.tracking.model.dto.request.TrackingParam;
import com.sample.tracking.model.dto.response.TrackingResponse;
import com.sample.tracking.service.internal.TrackingInternalService;
import com.sample.tracking.util.TrackingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(TrackingController.class)
public class TrackingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TrackingInternalService trackingInternalService;

    private TrackingParam trackingParam;
    private TrackingResponse trackingResponse;

    @BeforeEach
    void setUp(){
        var custId = UUID.randomUUID().toString();
        var createdAt = "2025-07-27T22:48:47.45+08:00";

        trackingParam = TrackingParam.builder()
                .originCountryId("ID")
                .destinationCountryId("US")
                .weight(BigDecimal.valueOf(10.5))
                .createdAt(OffsetDateTime.parse(createdAt).toLocalDateTime())
                .customerId(custId)
                .customerName("John Doe")
                .customerSlug("john-doe")
                .build();

        var trxNumb = TrackingUtil.generateTrackingNumber(trackingParam.getOriginCountryId(), trackingParam.getCreatedAt());


        trackingResponse = TrackingResponse.builder()
                .trackingNumber(trxNumb)
                .createdAt(LocalDateTime.now())
                .customerId(custId)
                .customerName("John Doe")
                .customerSlug("john-doe")
                .originCountryId("ID")
                .destinationCountryId("US")
                .build();
    }

    @Test
    @DisplayName("Should successfully fetch next tracking number")
    void fetchNextTrackingNumber_success() {
        when(trackingInternalService.fetchNextTrackingNumber(any(TrackingParam.class)))
                .thenReturn(Mono.just(trackingResponse));

        Response<TrackingResponse> expectedResponse = Response.<TrackingResponse>builder()
                .message(Message.SUCCESS)
                .data(trackingResponse)
                .build();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(Path.TRACKING_NUMBER)
                        .queryParam("origin_country_id", trackingParam.getOriginCountryId())
                        .queryParam("destination_country_id", trackingParam.getDestinationCountryId())
                        .queryParam("weight", trackingParam.getWeight())
                        .queryParam("created_at", trackingParam.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .queryParam("customer_id", trackingParam.getCustomerId())
                        .queryParam("customer_name", trackingParam.getCustomerName())
                        .queryParam("customer_slug", trackingParam.getCustomerSlug())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return success message with null data when service encounters an error")
    void fetchNextTrackingNumber_serviceError() {
        when(trackingInternalService.fetchNextTrackingNumber(any(TrackingParam.class)))
                .thenReturn(Mono.error(new RuntimeException("Service internal error")));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(Path.TRACKING_NUMBER)
                        .queryParam("origin_country_id", trackingParam.getOriginCountryId())
                        .queryParam("destination_country_id", trackingParam.getDestinationCountryId())
                        .queryParam("weight", trackingParam.getWeight())
                        .queryParam("created_at", trackingParam.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .queryParam("customer_id", trackingParam.getCustomerId())
                        .queryParam("customer_name", trackingParam.getCustomerName())
                        .queryParam("customer_slug", trackingParam.getCustomerSlug())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
