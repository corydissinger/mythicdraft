package com.cd.mythicdraft.etl.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

	private static final Logger logger = Logger.getLogger(SchedulerConfig.class);	
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job statsJob;
	
	@Autowired
	private Job draftFormatMigration;
	
	//Every 30 minutes
	@Scheduled(fixedDelay = 30 * 60 * 1000, initialDelay = 60 * 1000)
	public void statsJob() {
		try {
			jobLauncher.run(statsJob, new JobParameters());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} 
	}

	//Easy job, run always
	@Scheduled(fixedDelay = 15 * 1000)
	public void draftFormatMigration() {
		try {
			jobLauncher.run(draftFormatMigration, new JobParameters());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} 
	}
}
