package com.scheduler.dao;


import com.scheduler.entity.SchedulerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerDetails, Long> {
    Optional<SchedulerDetails> findByName(String emailScheduler);
}
