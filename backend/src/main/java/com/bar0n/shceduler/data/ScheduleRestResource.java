package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.Schedule;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by dbaron
 */
@RepositoryRestResource(collectionResourceRel = "schedule", path = "schedule")
interface ScheduleRestResource extends PagingAndSortingRepository<Schedule, Long> {
}
