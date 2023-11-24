package com.scheduler.controller;

import com.scheduler.dto.SchedulerDetailsDto;
import com.scheduler.service.SchedulerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scheduler-config")
public class SchedulerController {

    @Autowired
    SchedulerManager schedulerManager;
    @GetMapping("/all")
    public ResponseEntity<List<SchedulerDetailsDto>> getAllConfigs(){
        List<SchedulerDetailsDto> schedulerDetails =
            schedulerManager.getAllScheduleConfigs();
        return new ResponseEntity<>(schedulerDetails, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<SchedulerDetailsDto> saveSchedulerConfig(@RequestBody SchedulerDetailsDto schedulerDetailsDto){
        return new ResponseEntity<>(schedulerManager.saveSchedulerConfig(schedulerDetailsDto), HttpStatus.OK);
    }

}
