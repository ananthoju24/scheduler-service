package com.scheduler.schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class EmailScheduler implements Runnable{
    Logger logger = LoggerFactory.getLogger(EmailScheduler.class);

    private final List<String> emailIDs = Arrays.asList("test1@gmail.com","test2@gmail.com","test3@gmail.com","test4@gmail.com");

    @Override
    public void run() {
        logger.info("Processing Emails to be triggered");
        emailIDs.forEach( email -> logger.info(" Sending mail to "+email));
    }
}
