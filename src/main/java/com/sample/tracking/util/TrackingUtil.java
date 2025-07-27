package com.sample.tracking.util;

import com.sample.tracking.model.constant.TrackingConstant;
import com.sample.tracking.model.dto.request.TrackingParam;
import com.sample.tracking.model.dto.response.TrackingResponse;
import com.sample.tracking.model.entity.Tracking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TrackingUtil {
    private TrackingUtil(){}

    public static String generateTrackingNumber(String origin, LocalDateTime dateTime){
        var uniqueNumber = fetchRandomUniqueNumber();
        var uniqueIdentity = fetchRandomUniqueIdentity(2);
        var period = fetchPeriod(dateTime);

        return TrackingConstant.PREFIX + uniqueIdentity + origin + period + uniqueNumber ;
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(TrackingConstant.FORMAT_DATE);

        return dateTime.format(dateTimeFormatter);
    }

    public static Tracking generateTracking(String trxNumber, TrackingParam trackingParam){
        return new Tracking();
    }

    public static TrackingResponse generateResponse(Tracking tracking){
        return TrackingResponse.builder().build();
    }
}
