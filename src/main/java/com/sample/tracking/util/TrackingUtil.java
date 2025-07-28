package com.sample.tracking.util;

import com.sample.tracking.model.constant.Constant;
import com.sample.tracking.model.dto.common.CustomerInfo;
import com.sample.tracking.model.dto.request.TrackingParam;
import com.sample.tracking.model.dto.response.TrackingResponse;
import com.sample.tracking.model.entity.Tracking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

public class TrackingUtil {
    private TrackingUtil(){}

    public static TrackingParam generateTrackingParam(Map<String, String> params){
        return TrackingParam.builder()
                .originCountryId(params.get("origin_country_id"))
                .destinationCountryId(params.get("destination_country_id"))
                .createdAt(OffsetDateTime.parse(params.get("created_at")).toLocalDateTime())
                .weight(new BigDecimal(params.get("weight")))
                .customerId(params.get("customer_id"))
                .customerName(params.get("customer_name"))
                .customerSlug(params.get("customer_slug"))
                .build();
    }

    public static String generateTrackingNumber(String origin, LocalDateTime dateTime){
        var uniqueNumber = fetchRandomUniqueNumber();
        var uniqueIdentity = fetchRandomUniqueIdentity(2);
        var period = fetchPeriod(dateTime);

        return Constant.PREFIX + uniqueIdentity + origin + period + uniqueNumber ;
    }

    public static String fetchRandomUniqueNumber(){
        Random random = new Random();
        return String.valueOf(random.nextInt(10000));
    }

    public static String fetchRandomUniqueIdentity(int length){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i=0; i<length; i++){
            char randomChar = (char) ('A' + random.nextInt(26));
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public static String fetchPeriod(LocalDateTime dateTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constant.FORMAT_DATE);

        return dateTime.format(dateTimeFormatter);
    }

    public static Tracking generateTracking(String trxNumber, TrackingParam trackingParam){
        return Tracking.builder()
                .trackingNumber(trxNumber)
                .originCountryId(trackingParam.getOriginCountryId())
                .destinationCountryId(trackingParam.getDestinationCountryId())
                .weight(trackingParam.getWeight())
                .createdAt(trackingParam.getCreatedAt())
                .customerInfo(generateCustomerInfo(trackingParam))
                .build();
    }

    public static CustomerInfo generateCustomerInfo(TrackingParam trackingParam){
        return CustomerInfo.builder()
                .customerId(trackingParam.getCustomerId())
                .customerName(trackingParam.getCustomerName())
                .customerSlug(trackingParam.getCustomerSlug())
                .build();
    }

    public static TrackingResponse generateResponse(Tracking tracking){
        return TrackingResponse.builder()
                .trackingNumber(tracking.getTrackingNumber())
                .createdAt(tracking.getCreatedAt())
                .customerId(tracking.getCustomerInfo().getCustomerId())
                .customerName(tracking.getCustomerInfo().getCustomerName())
                .customerSlug(tracking.getCustomerInfo().getCustomerSlug())
                .originCountryId(tracking.getOriginCountryId())
                .destinationCountryId(tracking.getDestinationCountryId())
                .build();
    }
}
