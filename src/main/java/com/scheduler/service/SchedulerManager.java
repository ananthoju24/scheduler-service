package com.scheduler.service;

import com.scheduler.dto.SchedulerDetailsDto;
import com.scheduler.schedulers.EmailScheduler;
import com.scheduler.dao.SchedulerRepository;
import com.scheduler.entity.SchedulerDetails;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
public class SchedulerManager {

    Logger logger = LoggerFactory.getLogger(SchedulerManager.class);
    @Autowired
    SchedulerRepository schedulerRepository;

    @Autowired
    TaskScheduler taskScheduler;

    //@Autowired
    //SchedulerMapper schedulerMapper;

    Map<String, ScheduledFuture<?>> scheduledTaskMap = new HashMap<>();

    @PostConstruct
    public void init() {
        logger.info("String student service ");
        SchedulerDetails schedulerMetaData = new SchedulerDetails();
        schedulerMetaData.setName("EmailScheduler");
        schedulerMetaData.setCornExpression("0 0/1 * * * ?");// runs every 1 min
        schedulerMetaData.setCreateTime(LocalTime.now());
        schedulerMetaData.setUpdateTime(LocalTime.now());
        schedulerRepository.save(schedulerMetaData);

        List<SchedulerDetails> schedulerMetaDataList = schedulerRepository.findAll();
        schedulerMetaDataList.forEach(s -> logger.info("" + s));

        logger.info("Schedule the Email Task ");
        // TODO : We have to pass the runnable based on the received config/scheduler
        schedulerMetaDataList.forEach(schedulerDetails -> scheduleTask(new EmailScheduler(), schedulerDetails.getName(), schedulerDetails.getCornExpression()));
    }

    public List<SchedulerDetailsDto> getAllScheduleConfigs() {
        List<SchedulerDetails> allConfig = schedulerRepository.findAll();
        return allConfig.stream().map(this::convertToSchedulerDto).collect(Collectors.toList());
    }

    public SchedulerDetailsDto saveSchedulerConfig(SchedulerDetailsDto schedulerDetailsDto) {
        logger.info("Request to save  or update scheduler config {}", schedulerDetailsDto);
        Optional<SchedulerDetails> sDetails = schedulerRepository.findByName(schedulerDetailsDto.getName());
        SchedulerDetails schedulerDetails = schedulerRepository.save(convertToSchedulerEntity(schedulerDetailsDto, sDetails.get()));
        cancelScheduledTask(schedulerDetails.getName());
        scheduleTask(new EmailScheduler(), schedulerDetails.getName(), schedulerDetails.getCornExpression());
        return convertToSchedulerDto(schedulerDetails);
    }

    private void scheduleTask(Runnable runnable, String name, String cronExpresion) {
        logger.info("Scheduling {} ", name);
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(new Thread(runnable), new CronTrigger(cronExpresion));
        scheduledTaskMap.put(name, scheduledFuture);
    }

    private void cancelScheduledTask(String name) {
        scheduledTaskMap.get(name).cancel(true);
        try {
            logger.info("After executing cancel on scheduler will wait for 10 secs ");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Scheduler {} cancelled ", name);
    }


    private SchedulerDetailsDto convertToSchedulerDto(SchedulerDetails schedulerDetails) {
        SchedulerDetailsDto schedulerDetailsDto = new SchedulerDetailsDto();
        schedulerDetailsDto.setId(schedulerDetails.getId());
        schedulerDetailsDto.setName(schedulerDetails.getName());
        schedulerDetailsDto.setCornExpression(schedulerDetails.getCornExpression());
        schedulerDetailsDto.setCreateTime(schedulerDetails.getCreateTime());
        schedulerDetailsDto.setUpdateTime(schedulerDetails.getUpdateTime());
        return schedulerDetailsDto;
    }

    private SchedulerDetails convertToSchedulerEntity(SchedulerDetailsDto schedulerDetailsDto, SchedulerDetails schedulerDetails) {
        if (Objects.isNull(schedulerDetails)) {
            schedulerDetails = new SchedulerDetails();
            schedulerDetails.setCreateTime(LocalTime.now());
            schedulerDetails.setName(schedulerDetailsDto.getName());
        }
        schedulerDetails.setCornExpression(schedulerDetailsDto.getCornExpression());
        schedulerDetails.setUpdateTime(LocalTime.now());
        return schedulerDetails;
    }
}
