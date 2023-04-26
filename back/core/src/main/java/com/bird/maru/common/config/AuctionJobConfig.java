package com.bird.maru.common.config;

import com.bird.maru.AuctionLog.repository.AuctionLogRepository;
import com.bird.maru.AuctionLog.service.AuctionLogService;
import com.bird.maru.domain.model.entity.AuctionLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuctionJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AuctionLogRepository auctionLogsRepository;

    private final AuctionLogService auctionService;

    @Bean
    public Job auctionLogsJob(Step auctionLogsStep) {
        return jobBuilderFactory.get("auctionLogsJob")
                .incrementer(new RunIdIncrementer())
                .start(auctionLogsStep)
                .build();
    }

    LocalDateTime now = LocalDateTime.now();
    static LocalDateTime alarmDate;
    public void setAlarmDate() {
        alarmDate = now.minusDays(7);
    }

    @JobScope
    @Bean
    public Step auctionLogsStep(
            ItemReader<AuctionLog> auctionLogsReader,
            ItemProcessor<AuctionLog, AuctionLog> auctionLogsProcessor){

        setAlarmDate();

        return stepBuilderFactory.get("auctionLogsStep")
                .<AuctionLog, AuctionLog>chunk(5)
                .reader(auctionLogsReader)
                .writer(new ItemWriter() {
                    @Override
                    public void write(List items) {
                        System.out.println("출력중");
                        items.forEach(System.out::println);
                        System.out.println("리스트 크기 : " + items.size());
                        System.out.println("출력중2");
                    }
                })
                .processor(auctionLogsProcessor)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<AuctionLog> auctionLogsReader() {
        log.info("== auctionLogsReader 실행중 ==");
        return new RepositoryItemReaderBuilder<AuctionLog>()
                .name("auctionLogsReader")
                .repository(auctionLogsRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<AuctionLog, AuctionLog> auctionLogsProcessor(){
        log.info("== auctionLogsProcessor 실행중 ==");
        return new ItemProcessor<AuctionLog, AuctionLog>() {
            @Override
            public AuctionLog process(AuctionLog item) throws Exception {
                auctionService.auctionExecute(item);
                return new AuctionLog(item);
            }
        };
    }
}
