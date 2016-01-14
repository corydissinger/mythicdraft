package com.cd.mythicdraft.config;

import javax.sql.DataSource;

import org.apache.commons.lang3.tuple.ImmutablePair;
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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cd.mythicdraft.batch.DraftFormatProcessor;
import com.cd.mythicdraft.batch.DraftFormatWriter;
import com.cd.mythicdraft.batch.StatsProcessor;
import com.cd.mythicdraft.batch.FormatProcessor;
import com.cd.mythicdraft.batch.FormatWriter;
import com.cd.mythicdraft.batch.StatsWriter;
import com.cd.mythicdraft.batch.jdbc.DraftRowMapper;
import com.cd.mythicdraft.batch.jdbc.StatsRowMapper;
import com.cd.mythicdraft.dao.StatsSql;
import com.cd.mythicdraft.model.Card;
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
    			.<Integer, Format> chunk(10)
    			.reader(draftReader())
    			.processor(formatProcessor())
    			.writer(formatWriter())
    			.build();
    }
    
    @Bean
    protected Step migrateDrafts() {
    	return steps.get("migrateDrafts")
    			.<Integer, Draft> chunk(10)
    			.reader(draftReader())
    			.processor(migrateDraftProcessor())
    			.writer(migrateDraftWriter())
    			.build();
    }    

	@Bean
    protected ItemReader<Integer> draftReader() {
    	JdbcCursorItemReader<Integer> itemReader = new JdbcCursorItemReader<Integer>();
    	itemReader.setDataSource(dataSource);
    	itemReader.setSql(StatsSql.GET_DRAFTS_WITHOUT_FORMAT);
    	itemReader.setRowMapper(new DraftRowMapper());
    	return itemReader;
    }
    
    @Bean
    protected ItemProcessor<Integer, Format> formatProcessor() {
    	return new FormatProcessor();
    }
    
    @Bean
    protected ItemWriter<Format> formatWriter() {
		return new FormatWriter();
	}
    
    @Bean
    protected ItemProcessor<Integer, Draft> migrateDraftProcessor() {
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
    			.<ImmutablePair<Card, Format>, FormatPickStats> chunk(10)
    			.reader(statsReader())
    			.processor(statsProcessor())
    			.writer(statsWriter())
    			.build();
    }    
    
	@Bean
    protected ItemReader<ImmutablePair<Card, Format>> statsReader() {
    	JdbcCursorItemReader<ImmutablePair<Card, Format>> itemReader = new JdbcCursorItemReader<ImmutablePair<Card, Format>>();
    	itemReader.setDataSource(dataSource);
    	itemReader.setSql(StatsSql.GET_FORMATS_AND_CARDS);
    	itemReader.setRowMapper(new StatsRowMapper());
    	return itemReader;
    }    
    
	@Bean
    protected ItemProcessor<ImmutablePair<Card, Format>, FormatPickStats> statsProcessor() {
    	return new StatsProcessor();
    }
	
	@Bean
	protected ItemWriter<FormatPickStats> statsWriter() {
		return new StatsWriter();
	}
}
