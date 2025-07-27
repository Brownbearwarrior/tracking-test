package com.sample.tracking.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackingParam {
    private String originCountryId;
    private String destinationCountryId;
    private BigDecimal weight;
    private LocalDateTime createdAt;
    private String customerId;
    private String customerName;
    private String customerSlug;
}
