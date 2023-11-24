package com.scheduler.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class SchedulerDetailsDto {

    private Long id;
    private String name;
    private String cornExpression;
    private LocalTime createTime;
    private LocalTime updateTime;
}
