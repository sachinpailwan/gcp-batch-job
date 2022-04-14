package com.db.flare.gcpbatchjob.job;

import com.db.flare.gcpbatchjob.common.FlareSkippableException;
import com.db.flare.gcpbatchjob.model.Payment;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.util.Collections;

@Configuration
public class FlareJob {

    @Autowired
    private JobRegistry jobRegistry;

    @Bean
    @StepScope
    public Resource resource(@Value("#{jobParameters['input-file']}") String fileName) throws MalformedURLException {
        return new UrlResource(fileName);
    }



    @Bean
    public FlatFileItemReader<Payment> itemReader(Resource resource)  {
        return new FlatFileItemReaderBuilder<Payment>()
                .name("personItemReader")
                .resource(resource)
                .linesToSkip(1)
                .delimited()
                .names("transaction_id", "ordering","beneficiary","date","amount")
                .targetType(Payment.class)
                .build();
    }

    @Bean
    public ItemWriter<Payment> itemWriter() {
        XStreamMarshaller paymentMarshaller = new XStreamMarshaller();

        Resource exportFileResource = new FileSystemResource("result.xml");

        paymentMarshaller.setAliases(Collections.singletonMap("payment",Payment.class));
        return new StaxEventItemWriterBuilder<Payment>()
                .name("paymentWriter")
                .resource(exportFileResource)
                .marshaller(paymentMarshaller)
                .rootTagName("payments")
                .build();
    }

    @Bean
    public Job job(JobBuilderFactory jobs, StepBuilderFactory steps,
                   DataSource dataSource, Resource resource) {
        return jobs.get("flare-job")
                .start(steps.get("step")
                        .<Payment, Payment>chunk(3)
                        .faultTolerant()
                        .skip(FlareSkippableException.class)
                        .skipLimit(Integer.MAX_VALUE)
                        .reader(itemReader(resource))
                        .processor(itemProcessor())
                        .writer(itemWriter())
                        .build())
                .build();
    }
    @Bean
    public ItemProcessor<Payment,Payment> itemProcessor(){
        return new PaymentProcessor();
    }
}
