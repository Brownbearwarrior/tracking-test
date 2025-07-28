package com.sample.tracking.repository;

import com.sample.tracking.model.entity.Tracking;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRepository extends ReactiveMongoRepository<Tracking, String> {
}
