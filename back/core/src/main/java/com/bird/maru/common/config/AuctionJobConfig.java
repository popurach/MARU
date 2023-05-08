package com.bird.maru.common.config;

import com.bird.maru.auction.repository.AuctionRepository;
import com.bird.maru.auctionlog.repository.AuctionLogRepository;
import com.bird.maru.auctionlog.repository.query.AuctionLogCustomQueryRepository;
import com.bird.maru.auctionlog.service.AuctionLogService;
import com.bird.maru.common.util.NamedLockExecutor;
import com.bird.maru.domain.model.entity.Auction;
import com.bird.maru.domain.model.entity.AuctionLog;
import com.bird.maru.domain.model.entity.Landmark;
import com.bird.maru.landmark.repository.LandmarkRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Flow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuctionJobConfig {

    private final NamedLockExecutor namedLockExecutor;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AuctionLogRepository auctionLogsRepository;
    private final LandmarkRepository landmarkRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionLogCustomQueryRepository auctionLogCustomQueryRepository;
    private final AuctionLogService auctionService;

    @Bean
    public Job auctionLogsJob(
            Step auctionLogsStep,
            Step conditionalFailStep,
            Step conditionalCompletedStep,
            Step finishAuctionTableStep,
            Step createAuctionTableStep
    ) {
        return jobBuilderFactory.get("auctionLogsJob")
                                .incrementer(new RunIdIncrementer())
                                .start(auctionLogsStep)
                                .on("FAILED").to(conditionalFailStep)
//                                .next(createAuctionTableStep)
                                .from(auctionLogsStep)
                                .on("*").to(conditionalCompletedStep)
                                .next(finishAuctionTableStep)
//                                .next(createAuctionTableStep)
                                .end()
                                .build();

    }

    /**
     * 경매 입찰 테이블(auctionLog) 확인 후 낙찰, 유찰 처리
     */
    @JobScope
    @Bean
    public Step auctionLogsStep(
            ItemReader<AuctionLog> auctionLogsReader,
            ItemProcessor<AuctionLog, AuctionLog> auctionLogsProcessor
    ) {
        return stepBuilderFactory.get("auctionLogsStep")
                                 .<AuctionLog, AuctionLog>chunk(5)
                                 .reader(auctionLogsReader)
                                 .writer(new ItemWriter() {
                                     @Override
                                     public void write(List items) {
                                         items.forEach(System.out::println);
                                     }
                                 })
                                 .processor(auctionLogsProcessor)
                                 .build();
    }

    @JobScope
    @Bean
    public Step conditionalFailStep() {
        return stepBuilderFactory.get("conditionalFailStep")
                                 .tasklet(new Tasklet() {
                                     @Override
                                     public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                                         log.info("Auction Batch Job 실패");
                                         return RepeatStatus.FINISHED;
                                     }
                                 })
                                 .build();
    }

    @JobScope
    @Bean
    public Step conditionalCompletedStep() {
        return stepBuilderFactory.get("conditionalCompletedStep")
                                 .tasklet(new Tasklet() {
                                     @Override
                                     public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                                         log.info("Auction Logs Step 성공");
                                         return RepeatStatus.FINISHED;
                                     }
                                 })
                                 .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<AuctionLog> auctionLogsReader() {
        log.info("== auctionLogsReader 실행중 ==");
        return new RepositoryItemReaderBuilder<AuctionLog>()
                .name("auctionLogsReader")
                .repository(auctionLogsRepository)
                .methodName("findAllByAuction_Finished")
                .pageSize(5)
                .arguments(false)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<AuctionLog, AuctionLog> auctionLogsProcessor() {
        log.info("== auctionLogsProcessor 실행중 ==");
        return item -> {
            namedLockExecutor.executeWithLock(
                    // Batch 작업 DB Named Lock 실시
                    item.getId().toString(), 10, () -> auctionService.auctionExecute(item)
            );

            return item;
        };
    }

    /**
     * Auction 테이블 finished 0 -> 1로 모두 변경
     */
    @JobScope
    @Bean
    public Step finishAuctionTableStep(
            ItemReader<Auction> auctionsReader,
            ItemProcessor<Auction, Auction> auctionsProcessor,
            ItemWriter<Auction> auctionsWriter
    ) {
        log.info("== finishAuctionTableStep 실행중 ==");
        return stepBuilderFactory.get("finishAuctionTableStep")
                                 .<Auction, Auction>chunk(5)
                                 .reader(auctionsReader)
                                 .processor(auctionsProcessor)
                                 .writer(auctionsWriter)
                                 .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Auction> auctionsReader() {
        log.info("== auctionsReader 실행중 ==");
        return new RepositoryItemReaderBuilder<Auction>()
                .name("auctionsReader")
                .repository(auctionRepository)
                .methodName("findByFinished")
                .pageSize(5)
                .arguments(false)
                .sorts(Collections.singletonMap("createdDate", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Auction, Auction> auctionsProcessor() {
        log.info("== auctionsProcessor 실행중 ==");
//        return new ItemProcessor<Auction, Auction>() {
//            @Override
//            public Auction process(Auction auction) throws Exception {
//                log.info("완료 처리 할 auction : {} {}", auction.getLastLogId(), auction.getFinished());
////                auction.changeFinished();
//                auction.setFinished(true);
//                log.info("완료 처리!!! auction : {} {}", auction.getLastLogId(), auction.getFinished());
//                return auction;
//            }
//        };
        return item -> {
            namedLockExecutor.executeWithLock(
                    // Batch 작업 DB Named Lock 실시
                    item.getCreatedDate().toString() + item.getLandmark().getId().toString(), 10, () -> item.setFinished(true)
            );

            return item;
        };
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<Auction> auctionsWriter() {
        log.info("Auctions All Finished Completely");
        return new RepositoryItemWriterBuilder<Auction>()
                .repository(auctionRepository)
                .methodName("save")
                .build();
    }

    /**
     * 경매 낙찰 Step 이후 Auction 테이블 생성 Step
     */
    @JobScope
    @Bean
    public Step createAuctionTableStep(
            ItemReader<Landmark> landmarkReader,
            ItemProcessor<Landmark, Auction> landmarkProcessor,
            ItemWriter<Auction> auctionsWriter
//            ItemWriter<Auction> landmarkWriter
    ) {
        return stepBuilderFactory.get("createAuctionTable")
                                 .<Landmark, Auction>chunk(5)
                                 .reader(landmarkReader)
                                 .processor(landmarkProcessor)
                                 .writer(auctionsWriter)
                                 .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Landmark> landmarkReader() {
        log.info("== landmarkReader 실행중 ==");
        return new RepositoryItemReaderBuilder<Landmark>()
                .name("landmarkReader")
                .repository(landmarkRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Landmark, Auction> landmarkProcessor() {
        return new ItemProcessor<Landmark, Auction>() {
            @Override
            public Auction process(Landmark landmark) throws Exception {
                return Auction.builder()
                              .createdDate(LocalDate.now())
                              .landmark(landmark)
                              .build();
            }
        };
    }

//    @StepScope
//    @Bean
//    public RepositoryItemWriter<Auction> landmarkWriter() {
//        return new RepositoryItemWriterBuilder<Auction>()
//                .repository(auctionRepository)
//                .methodName("save")
//                .build();
//    }

}
