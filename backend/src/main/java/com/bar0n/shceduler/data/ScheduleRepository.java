package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dbaron
 */
//@RepositoryRestResource(collectionResourceRel = "schedule", path = "schedule")
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
