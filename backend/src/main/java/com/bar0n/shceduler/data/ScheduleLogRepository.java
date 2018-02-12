package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.ScheduleLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dbaron
 */
//@RepositoryRestResource(collectionResourceRel = "scheduleLog", path = "scheduleLog")
@Repository
public interface ScheduleLogRepository extends JpaRepository<ScheduleLog, Long> {
    Page<ScheduleLog> findAllByOrderByIdDesc(Pageable pageable);
}
