package com.sample.tracking.model.dto.common;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerInfo {
    private String customerId;
    private String customerName;
    private String customerSlug;
}
