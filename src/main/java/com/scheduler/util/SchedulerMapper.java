package com.scheduler.util;

import com.scheduler.dto.SchedulerDetailsDto;
import com.scheduler.entity.SchedulerDetails;


public interface SchedulerMapper {

    SchedulerDetailsDto toSchedulerDto(SchedulerDetails schedulerDetails);
    SchedulerDetails toSchedulerEntity(SchedulerDetailsDto schedulerDetailsDto);

}
