package shopping.whworker.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import shopping.whworker.domain.Product;
import shopping.whworker.processor.ProductItemProcessor;

import java.io.IOException;

@Configuration
public class BatchConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<Product> itemReader(@Value("#{jobParameters[inputFile]}") Resource resource) throws IOException {
        System.out.println("Resolved Input File Path: " + resource.getURI());

        return new FlatFileItemReaderBuilder<Product>().name("itemReader")
                .resource(resource)
                .delimited()
                .names("name", "credit")
                .targetType(Product.class)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Product> itemWriter(
            @Value("#{jobParameters[outputFile]}") String outputFilePath) {

        System.out.println("Resolved Output File Path: " + outputFilePath);
        Resource resource = new FileSystemResource(outputFilePath);
        return new FlatFileItemWriterBuilder<Product>()
                .name("itemWriter")
                .resource((WritableResource) resource)
                .delimited()
                .names("name", "credit")
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                   ItemReader<Product> itemReader, ItemWriter<Product> itemWriter) {
        return new JobBuilder("ioSampleJob", jobRepository)
                .start(new StepBuilder("step1", jobRepository).<Product, Product>chunk(1, transactionManager)
                        .reader(itemReader)
                        .processor(new ProductItemProcessor())
                        .writer(itemWriter)
                        .build())
                .build();
    }
}