package com.db.flare.gcpbatchjob;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableBatchProcessing
public class GcpBatchJobApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder()
				.sources(GcpBatchJobApplication.class)
				.web(WebApplicationType.NONE)
				.build();
		final ConfigurableApplicationContext run = application.run(args);
		final int exit = SpringApplication.exit(run);
		System.exit(exit);
	}

}
