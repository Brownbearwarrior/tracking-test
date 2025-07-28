package com.sample.tracking.model.entity;

import com.sample.tracking.model.constant.DocumentName;
import com.sample.tracking.model.dto.common.CustomerInfo;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = DocumentName.TRACKING)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tracking {

    @Id
    private String trackingNumber;

    private String originCountryId;
    private String destinationCountryId;
    private BigDecimal weight;
    private LocalDateTime createdAt;
    private CustomerInfo customerInfo;
}
