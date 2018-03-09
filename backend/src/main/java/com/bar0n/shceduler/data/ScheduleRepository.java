package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by dbaron
 */
//@RepositoryRestResource(collectionResourceRel = "schedule", path = "schedule")
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByNextLessThanAndActiveTrue(ZonedDateTime time);

    Page<Schedule> findAllByOrderByIdDesc(Pageable pageable);
}
