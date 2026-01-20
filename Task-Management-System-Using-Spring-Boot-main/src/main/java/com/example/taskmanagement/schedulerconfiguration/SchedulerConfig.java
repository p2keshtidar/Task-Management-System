package com.example.taskmanagement.schedulerconfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import lombok.extern.slf4j.Slf4j;

/**
 * @Configuration class for setting up the task scheduler.
 * 
 *                <p>
 *                this class defines a Spring bean for a
 *                <strong> @ThreadPoolTaskScheduler </strong>, which allows for
 *                scheduling of tasks in a multi-threaded environment. The
 *                scheduler is configured with a specific pool size and thread
 *                naming convention to facilitate monitoring and debugging.
 *                </p>
 */
@Configuration
@Slf4j
public class SchedulerConfig {

	private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

	/**
	 * Creates and configures a <strong> @ThreadPoolTaskScheduler </strong> bean.
	 * 
	 * @return a configured <strong> @ThreadPoolTaskScheduler </strong> instance
	 */
	@Bean
	ThreadPoolTaskScheduler taskScheduler() {
		logger.info("Configuring ThreadPoolTaskScheduler...");
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		/**
		 * set the number of threads in the pool. Default is 1.
		 */
		taskScheduler.setPoolSize(5);
		/**
		 * set the prefix for thread names. Default is "SimpleAsyncTaskExecutor - ".
		 */
		taskScheduler.setThreadNamePrefix("TaskScheduler-");
		logger.info("ThreadPoolTaskScheduler configured with pool size: {} ", taskScheduler.getPoolSize());
		return taskScheduler;
	}
}