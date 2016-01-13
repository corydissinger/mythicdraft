package com.cd.mythicdraft.service;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service("draftFormatMigrationService")
public class DraftFormatMigrationService implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger logger = Logger.getLogger(DraftFormatMigrationService.class);

	@Autowired
	private Environment environment;	
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job draftFormatMigration;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			jobLauncher.run(draftFormatMigration, new JobParameters());	
		} catch (Exception e) {
			logger.error("Failed to start draft format migration!");
			logger.error(e.getMessage());
		}
	}		

}
