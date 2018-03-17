package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.model.ScheduleLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by dbaron
 */
//@RepositoryRestResource(collectionResourceRel = "scheduleLog", path = "scheduleLog")
@Repository
public interface ScheduleLogRepository extends JpaRepository<ScheduleLog, Long> {
    Page<ScheduleLog> findAllByOrderByIdDesc(Pageable pageable);
    @Modifying
    @Transactional
    @Query("delete from ScheduleLog u where u.schedule.id = ?1")
    void deleteScheduleLogsByscheduleId(Long id);
    List<ScheduleLog> findByNextLessThanAndCompletedFalse(LocalDateTime time);
}
