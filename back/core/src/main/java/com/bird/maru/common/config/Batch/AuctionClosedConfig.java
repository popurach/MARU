package com.bird.maru.common.config.Batch;

import com.bird.maru.auctionlog.repository.AuctionLogRepository;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.bird.maru.notice.service.NoticeService;
import java.util.Collections;
import java.util.List;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuctionClosedConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AuctionLogRepository auctionLogRepository;
    private final NoticeService noticeService;

    @Bean
    public Job auctionClosedJob(Step auctionClosedStep) {
        return jobBuilderFactory.get("auctionClosedJob")
                                .incrementer(new RunIdIncrementer())
                                .start(auctionClosedStep)
                                .build();
    }

    @JobScope
    @Bean
    public Step auctionClosedStep(
            ItemReader<AuctionLog> auctionClosedItemReader,
            ItemProcessor<AuctionLog, AuctionLog> auctionClosedItemProcessor
    ) {
        return stepBuilderFactory.get("auctionClosedStep")
                                 .<AuctionLog, AuctionLog>chunk(5)
                                 .reader(auctionClosedItemReader)
                                 .writer(new ItemWriter() {
                                     @Override
                                     public void write(List items) {
                                         items.forEach(System.out::println);
                                     }
                                 })
                                 .processor(auctionClosedItemProcessor)
                                 .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<AuctionLog> auctionClosedItemReader() {
        log.info("== auctionClosedItemReader 실행중 ==");
        return new RepositoryItemReaderBuilder<AuctionLog>()
                .name("auctionClosedItemReader")
                .repository(auctionLogRepository)
                .methodName("findAllByAuction_Finished")
                .pageSize(5)
                .arguments(Boolean.FALSE)
                .sorts(Collections.singletonMap("id", Direction.ASC))
                .build();

    }

    @StepScope
    @Bean
    public ItemProcessor<AuctionLog, AuctionLog> auctionClosedItemProcessor() {
        log.info("== auctionClosedItemProcessor 실행중 ==");
        return new ItemProcessor<AuctionLog, AuctionLog>() {
            @Override
            public AuctionLog process(AuctionLog auctionLog) throws Exception {
                // 경매 참여자 : 경매 1시간 전 알림
                noticeService.notifyAuctionClosed(auctionLog.getMember());
                return auctionLog;
            }
        };
    }
}
