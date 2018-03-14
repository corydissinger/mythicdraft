package com.cd.mythicdraft.etl.config;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cd.mythicdraft.batch.DraftFormatProcessor;
import com.cd.mythicdraft.batch.DraftFormatWriter;
import com.cd.mythicdraft.batch.FormatProcessor;
import com.cd.mythicdraft.batch.FormatWriter;
import com.cd.mythicdraft.batch.StatsProcessor;
import com.cd.mythicdraft.batch.StatsWriter;
import com.cd.mythicdraft.dao.StatsSql;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

@Configuration
@EnableBatchProcessing
public class StatsConfig {

    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;	
	
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private SessionFactory sessionFactory;
    
    //Probably less than ideal, but currently not requiring logging of the batch jobs
    @Bean
    public JobRepository jobRepository() throws Exception {
    	ResourcelessTransactionManager tm = new ResourcelessTransactionManager();
    	MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(tm);
    	
    	mapJobRepositoryFactoryBean.setTransactionManager(tm);
    	
    	return mapJobRepositoryFactoryBean.getObject();
    }
    
    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        return simpleJobLauncher;    	
    }
    
    @Bean
    protected Job draftFormatMigration() {
    	return jobs.get("draftFormatMigration").start(createFormats()).next(migrateDrafts()).build();
    }
    
    @Bean
    protected Step createFormats() {
    	return steps.get("createFormats")
    			.allowStartIfComplete(true)
    			.<Format, Format> chunk(10)
    			.reader(formatReader())
    			.processor(formatProcessor())
    			.writer(formatWriter())
    			.build();
    }
    
    @Bean
    protected Step migrateDrafts() {
    	return steps.get("migrateDrafts")
    			.allowStartIfComplete(true)
    			.<Draft, Draft> chunk(10)
    			.reader(draftReader())
    			.processor(migrateDraftProcessor())
    			.writer(migrateDraftWriter())
    			.build();
    }    

	@Bean
    protected ItemReader<Format> formatReader() {
		HibernateCursorItemReader<Format> itemReader = new HibernateCursorItemReader<Format>();
		
    	itemReader.setQueryString(StatsSql.GET_NEW_FORMATS);
    	itemReader.setUseStatelessSession(true);
    	itemReader.setSaveState(false);
    	itemReader.setSessionFactory(sessionFactory);
    	
    	return itemReader;
    }    
    
	@Bean
    protected ItemReader<Draft> draftReader() {
		HibernateCursorItemReader<Draft> itemReader = new HibernateCursorItemReader<Draft>();
		
    	itemReader.setQueryString(StatsSql.GET_DRAFTS_WITHOUT_FORMAT);
    	itemReader.setUseStatelessSession(true);
    	itemReader.setSaveState(false);
    	itemReader.setSessionFactory(sessionFactory);
    	
    	return itemReader;
    }
    
    @Bean
    protected ItemProcessor<Format, Format> formatProcessor() {
    	return new FormatProcessor();
    }
    
    @Bean
    protected ItemWriter<Format> formatWriter() {
		return new FormatWriter();
	}
    
    @Bean
    protected ItemProcessor<Draft, Draft> migrateDraftProcessor() {
    	return new DraftFormatProcessor();
    }
    
    @Bean
    protected ItemWriter<Draft> migrateDraftWriter() {
		return new DraftFormatWriter();
	}
    
    //End Format Migration

    @Bean
    protected Job statsJob() {
    	return jobs.get("statsJob").start(statsStep()).build();
    }
    
    @Bean
    protected Step statsStep() {
    	return steps.get("statsStep")
    			.allowStartIfComplete(true)
    			.<Format, List<FormatPickStats>> chunk(1)
    			.reader(statsReader())
    			.processor(statsProcessor())
    			.writer(statsWriter())
    			.build();
    }    
    
	@Bean
    protected ItemReader<Format> statsReader() {
    	HibernateCursorItemReader<Format> itemReader = new HibernateCursorItemReader<Format>();
    	
    	itemReader.setQueryString(StatsSql.GET_FORMATS);
    	itemReader.setUseStatelessSession(true);
    	itemReader.setSaveState(false);
    	itemReader.setSessionFactory(sessionFactory);
    	
    	return itemReader;
    }    
    
	@Bean
    protected ItemProcessor<Format, List<FormatPickStats>> statsProcessor() {
    	return new StatsProcessor();
    }
	
	@Bean
	protected ItemWriter<List<FormatPickStats>> statsWriter() {
		return new StatsWriter();
	}
}
