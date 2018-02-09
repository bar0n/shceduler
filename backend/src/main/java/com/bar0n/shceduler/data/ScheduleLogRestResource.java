package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.ScheduleLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by dbaron
 */
@RepositoryRestResource(collectionResourceRel = "scheduleLog", path = "scheduleLog")
interface ScheduleLogRestResource extends PagingAndSortingRepository<ScheduleLog, Long> {
}
