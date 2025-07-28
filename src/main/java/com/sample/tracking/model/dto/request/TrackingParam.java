package com.sample.tracking.model.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrackingParam {
    private String originCountryId;
    private String destinationCountryId;
    private BigDecimal weight;
    private LocalDateTime createdAt;
    private String customerId;
    private String customerName;
    private String customerSlug;
}
