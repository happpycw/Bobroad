package com.grepp.synapse4.infra.config;

import com.grepp.synapse4.app.model.restaurant.dto.create.CsvRestaurantDetailDto;
import com.grepp.synapse4.app.model.restaurant.dto.create.CsvRestaurantDto;
import com.grepp.synapse4.app.model.restaurant.dto.create.CsvRestaurantMenuDto;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.entity.RestaurantDetail;
import com.grepp.synapse4.app.model.restaurant.entity.RestaurantMenu;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import com.grepp.synapse4.infra.init.RestaurantDetailProcessor;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FilReaderJobConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final RestaurantDetailProcessor restaurantDetailProcessor;
    private final RestaurantRepository restaurantRepository;

    @Value("${batch.csv.basic-info:src/main/resources/restaurantdata/seoulBasic.csv}")
    private String basicInfoCsvPath;
    @Value("${batch.csv.detail-info:src/main/resources/restaurantdata/seoulDetail.csv}")
    private String detailInfoCsvPath;
    @Value("${batch.csv.menu-info:src/main/resources/restaurantdata/seoulMenu.csv}")
    private String menuInfoCsvPath;


    // N+1 문제를 위한 PublicId Set
    @JobScope
    @Bean
    public Set<String> preExistingRestaurantPublicIds(){
        log.info("📚 기존 식당 publicId 메모리에 로드...");
        List<String> publicIds = restaurantRepository.findAllPublicIds();
        Set<String> publicIdSet = new HashSet<>(publicIds);
        log.info("{} 개 기존 식당 publicId Set 완료", publicIdSet.size());

        return publicIdSet;
    }

    // N+1 문제를 위한 publicId와 식당Id map 생성
    @JobScope
    @Bean
    public Map<String, Restaurant> preLoadRestaurantMap(){
        log.info("📚 Restaurant Entity를 Map에 로드...");
        List<Restaurant> allRestaurants = restaurantRepository.findAll();
        Map<String, Restaurant> restaurantMap = allRestaurants.stream()
                .collect(Collectors.toMap(Restaurant::getPublicId,
                        restaurant -> restaurant));
        log.info("{} 개 식당 엔티티 Map 완료", restaurantMap.size());
        return restaurantMap;
    }




    @Bean
    public Job fileReadWriteJob(JobRepository jobRepository,                    // job-step 일련의 과정 히스토리를 기록하는 db 저장소
                                PlatformTransactionManager transactionManager)  // step 과정이 하나의 transaction 으로 처리되게끔 도와주는 객체
            throws Exception {

        return new JobBuilder("fileReadWriteJob", jobRepository)
                .incrementer(new RunIdIncrementer())    // job 실행 unique id
                .start(basicStep(jobRepository, transactionManager, preExistingRestaurantPublicIds()))
                .next(detailStep(jobRepository, transactionManager))
                .next(menuStep(jobRepository, transactionManager, preLoadRestaurantMap()))
                .build();
    }

    // Step 1 : 식당 기본 정보 데이터 저장
    // ItemReader - ItemProcessor - ItemWriter
    @JobScope
    @Bean
    public Step basicStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          Set<String> preExistingRestaurantPublicIds)
            throws Exception {

        return new StepBuilder("basicStep", jobRepository)
                .<CsvRestaurantDto, Restaurant>chunk(1000, transactionManager)
                .reader(basicFlatFileItemReader())
                .processor(basicFlatFileItemProcessor(preExistingRestaurantPublicIds))
                .writer(basicFlatFileItemWriter(entityManagerFactory))
                .faultTolerant()    // 실수 똘레랑스 : 모든 종류의 오류 허용
                .skip(Exception.class) // Exception 만나면 무조건 skip
                .skipLimit(Integer.MAX_VALUE) // 무조건 다 건너뜀~ , 설정 안 하면 기본값 0이므로 바로 터짐
                .listener(new SkipListener<CsvRestaurantDto, Restaurant>() {
                    private final Logger logger = LoggerFactory.getLogger(SkipListener.class);

                    @Override
                    public void onSkipInRead(Throwable t) {
                        if (t instanceof FlatFileParseException) {
                            FlatFileParseException e = (FlatFileParseException) t;
                            logger.error("‼️ basicStep " +
                                            "reader 오류: 라인 번호 [{}], 입력 [{}], 메시지: {}",
                                    e.getLineNumber(), e.getInput(), e.getMessage());
                        } else {
                            logger.error("‼️ basicStep " +
                                    "reader 오류: 메시지 [{}]", t.getMessage(), t);
                        }
                    }

                    @Override
                    public void onSkipInWrite(Restaurant item, Throwable t) {
                        logger.error("‼️ basicStep " +
                                "processor 오류: 아이템 [{}], 메시지 [{}]", item, t.getMessage());
                    }

                    @Override
                    public void onSkipInProcess(CsvRestaurantDto item, Throwable t) {
                        logger.error("‼️ basicStep " +
                                "writer 오류: 아이템 [{}], 메시지 [{}]", item, t.getMessage());
                    }
                })
                .build();
    }

    // Step 1 - ItemReader
    @StepScope
    @Bean
    public FlatFileItemReader<CsvRestaurantDto> basicFlatFileItemReader() throws Exception {

        log.info("1️⃣ Batch Step1 식당 정보 start!!!");

        return new FlatFileItemReaderBuilder<CsvRestaurantDto>()
                .name("basicFileItemReader")
                .resource(new FileSystemResource(basicInfoCsvPath))
                .encoding("UTF-8")
                .linesToSkip(1)
                .lineTokenizer(new DelimitedLineTokenizer(){{
                    // 기본 구분자(,) : 구분자를 기준으로 FieldSet을 만들어줌
                    setNames("publicId","name","branch","address","jibunAddress","latitude","longitude","tel","category","businessName","description");
                }})
//                .fieldSetMapper(new FieldSetMapper())     //csv 파일 헤더와 dto가 일치하지 않을 때 수동 매핑하기 위한 인터페이스
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CsvRestaurantDto>() {{        // 자동 매핑 : csv 헤더와 Dto의 필드가 완벽히 일치할 때만 사용
                    setTargetType(CsvRestaurantDto.class);
                }})
                .build();
    }

    // Step 1 - ItemProcessor
    // Dto -> Entity
    int basicCount =0;
    @StepScope
    @Bean
    public ItemProcessor<CsvRestaurantDto, Restaurant> basicFlatFileItemProcessor(Set<String> preExistingRestaurantPublicIds) {

        return dto -> {
            basicCount++;
            if (basicCount % 10000 == 0) {
                log.info("📖 Processed {} 개 식당 정보를 탐색했습니다.", basicCount);
            }

            // 데이터 유효성 검증
            // 위경도 값 없을 시 skip
            if (ObjectUtils.isEmpty(dto.getLatitude())
                    || ObjectUtils.isEmpty(dto.getLongitude())) {
                log.debug("위경도 값 부재 {}", dto.getName());
                return null;
            }
            // 이미 존재하는 publicId skip
            if(preExistingRestaurantPublicIds.contains(dto.getPublicId())){
                log.debug("이미 존재하는 publicId={}", dto.getPublicId());
                return null;
            }

            // restaurant 생성
            Restaurant restaurant = Restaurant.builder()
                    .publicId(dto.getPublicId())
                    .name(dto.getName())
                    .branch(dto.getBranch())
                    .roadAddress(dto.getAddress())
                    .jibunAddress(dto.getJibunAddress())
                    .latitude(dto.getLatitude())
                    .longitude(dto.getLongitude())
                    .category(dto.getCategory())
                    .build();

            // restaurant 저장
//            Restaurant savedRes = restaurantRepository.save(restaurant);

            // detail 생성
            // tel이 csv에는 basic에, 우리 DB에는 detail에 있어야 하기 때문
            // 아직 id 값은 없으므로 builder만 생성
            RestaurantDetail detail = RestaurantDetail.builder()
                    .restaurant(restaurant) // 연관관계
                    .tel(dto.getTel())
                    .build();

            restaurant.setDetail(detail);

            return restaurant;
        };
    }

    // Step 1 - ItemWriter
    // Entity DB에 저장!
    @StepScope
    @Bean
    public JpaItemWriter<Restaurant> basicFlatFileItemWriter(EntityManagerFactory entityManagerFactory) {

        JpaItemWriter<Restaurant> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }


    // Step 2 : 식당 상세 정보 데이터 저장
    @JobScope
    @Bean
    public Step detailStep(JobRepository jobRepository, PlatformTransactionManager transactionManager)
            throws Exception {

        log.info("2️⃣ Batch Step2 식당 상세 정보 start!!!");

        return new StepBuilder("detailStep", jobRepository)
                .<CsvRestaurantDetailDto, RestaurantDetail>chunk(1000, transactionManager)
                .reader(detailFlatFileItemReader())
                .processor(detailFlatFileItemProcessor(restaurantDetailProcessor))
                .writer(detailFlatFileItemWriter(entityManagerFactory))
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .listener(new SkipListener<CsvRestaurantDetailDto, Restaurant>() {
                    private final Logger logger = LoggerFactory.getLogger(SkipListener.class);

                    @Override
                    public void onSkipInRead(Throwable t) {
                        if (t instanceof FlatFileParseException) {
                            FlatFileParseException e = (FlatFileParseException) t;
                            logger.error("‼️ detailStep " +
                                            "reader 오류: 라인 번호 [{}], 입력 [{}], 메시지: {}",
                                    e.getLineNumber(), e.getInput(), e.getMessage());
                        } else {
                            logger.error("‼️ detailStep " +
                                    "reader 오류: 메시지 [{}]", t.getMessage(), t);
                        }
                    }

                    @Override
                    public void onSkipInWrite(Restaurant item, Throwable t) {
                        logger.error("‼️ detailStep " +
                                "processor 오류: 아이템 [{}], 메시지 [{}]", item, t.getMessage());
                    }

                    @Override
                    public void onSkipInProcess(CsvRestaurantDetailDto item, Throwable t) {
                        logger.error("‼️ detailStep " +
                                "writer 오류: 아이템 [{}], 메시지 [{}]", item, t.getMessage());
                    }
                })
                .build();
    }

    // Step 2 - ItemReader
    @StepScope
    @Bean
    public FlatFileItemReader<CsvRestaurantDetailDto> detailFlatFileItemReader() throws Exception {

        return new FlatFileItemReaderBuilder<CsvRestaurantDetailDto>()
                .name("detailFileItemReader")
                .resource(new FileSystemResource(detailInfoCsvPath))
                .encoding("UTF-8")
                .linesToSkip(1)
                .lineTokenizer(new DelimitedLineTokenizer(){{
                    // 기본 구분자(,) : 구분자를 기준으로 FieldSet을 만들어줌
                    setNames("publicId","name","branch","regionName","parkingRaw","wifiRaw","playroom","foreignMenu","toiletInfo","dayOff","rowBusinessTime","deliveryRow","onlineReservationInfo","homePageURL","landmarkName","landmarkLatitude","landmarkLongitude","distanceFromLandmark","smartOrder","signatureMenus","status","hashtags","areaInfo"

                    );
                }})

                .fieldSetMapper(new BeanWrapperFieldSetMapper<CsvRestaurantDetailDto>() {{        // 자동 매핑 : csv 헤더와 Dto의 필드가 완벽히 일치할 때만 사용
                    setTargetType(CsvRestaurantDetailDto.class);
                }})
                .build();
    }

    // Step 2 - ItemProcessor
    // Dto -> Entity
    // 같은 publicId를 가진 기본 식당 정보 데이터를 찾아 매핑해야 하므로, 별도 클래스로 분리
    @Bean
    @StepScope
    public ItemProcessor<CsvRestaurantDetailDto, RestaurantDetail> detailFlatFileItemProcessor(
            RestaurantDetailProcessor processor
    ) {
        return processor;
    }


    // Step 2 - ItemWriter
    // Entity DB에 저장!
    @StepScope
    @Bean
    public JpaItemWriter<RestaurantDetail> detailFlatFileItemWriter(EntityManagerFactory entityManagerFactory) {

        JpaItemWriter<RestaurantDetail> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }



    // Step 3 : 식당 메뉴 정보 데이터 저장
    @JobScope
    @Bean
    public Step menuStep(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         Map<String, Restaurant> preLoadRestaurantMap)
            throws Exception {

        log.info("3️⃣ Batch Step3 식당 메뉴 정보 start!!!");

        return new StepBuilder("menuStep", jobRepository)
                .<CsvRestaurantMenuDto, RestaurantMenu>chunk(1000, transactionManager)
                .reader(menuFlatFileItemReader())
                .processor(menuFlatFileItemProcessor(preLoadRestaurantMap))
                .writer(menuFlatFileItemWriter(entityManagerFactory))
                .faultTolerant()    // 실수 똘레랑스 : 모든 종류의 오류 허용
                .skip(Exception.class) // Exception 만나면 무조건 skip
                .skipLimit(Integer.MAX_VALUE) // 무조건 다 건너뜀~ , 설정 안 하면 기본값 0이므로 바로 터짐
                .listener(new SkipListener<CsvRestaurantMenuDto, Restaurant>() {
                    private final Logger logger = LoggerFactory.getLogger(SkipListener.class);

                    @Override
                    public void onSkipInRead(Throwable t) {
                        if (t instanceof FlatFileParseException) {
                            FlatFileParseException e = (FlatFileParseException) t;
                            logger.error("‼️ menuStep " +
                                            "reader 오류: 라인 번호 [{}], 입력 [{}], 메시지: {}",
                                    e.getLineNumber(), e.getInput(), e.getMessage());
                        } else {
                            logger.error("‼️ menuStep " +
                                    "reader 오류: 메시지 [{}]", t.getMessage(), t);
                        }
                    }

                    @Override
                    public void onSkipInWrite(Restaurant item, Throwable t) {
                        logger.error("‼️ menuStep " +
                                "processor 오류: 아이템 [{}], 메시지 [{}]", item, t.getMessage());
                    }

                    @Override
                    public void onSkipInProcess(CsvRestaurantMenuDto item, Throwable t) {
                        logger.error("‼️ menuStep " +
                                "writer 오류: 아이템 [{}], 메시지 [{}]", item, t.getMessage());
                    }
                })
                .build();
    }

    // Step 3 - ItemReader
    @StepScope
    @Bean
    public FlatFileItemReader<CsvRestaurantMenuDto> menuFlatFileItemReader() throws Exception {

        return new FlatFileItemReaderBuilder<CsvRestaurantMenuDto>()
                .name("menuFileItemReader")
                .resource(new FileSystemResource(menuInfoCsvPath))
                .encoding("UTF-8")
                .linesToSkip(1)
                .lineTokenizer(new DelimitedLineTokenizer(){{
                    // 기본 구분자(,) : 구분자를 기준으로 FieldSet을 만들어줌
                    setNames("publicMenuId","menuName","menuPrice","isLocalSpecialty","localSpecialtyName","localSpecialtySourceUrl","regionName","publicId","restaurantName","branchName"

                    );
                }})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CsvRestaurantMenuDto>() {{        // 자동 매핑 : csv 헤더와 Dto의 필드가 완벽히 일치할 때만 사용
                    setTargetType(CsvRestaurantMenuDto.class);
                }})
                .build();
    }

//    int menuCount=0;
    // Step 3 - ItemProcessor
    // Dto -> Entity
    // 같은 publicId를 가진 기본 식당 정보 데이터를 찾아 매핑해야 하므로, 별도 클래스로 분리
    @Bean
    @StepScope
    public ItemProcessor<CsvRestaurantMenuDto, RestaurantMenu> menuFlatFileItemProcessor(
            Map<String, Restaurant> preLoadRestaurantMap
    ) {
        basicCount = 0;
        return dto -> {

            basicCount++;
            if (basicCount % 10000 == 0) {
                log.info("🍘 Processed {} 개 메뉴 정보를 탐색했습니다.", basicCount);
            }

            Restaurant restaurant = preLoadRestaurantMap.get(dto.getPublicId());

            //(ObjectUtils.isEmpty(dto.getLatitude())
            if (ObjectUtils.isEmpty(restaurant)) {
                log.debug("Restaurant not found for publicId: {}", dto.getPublicId());
                return null;
            }

            return new RestaurantMenu(dto.getMenuName(), dto.getMenuPrice(), restaurant);
        };
    }


    // Step 3 - ItemWriter
    // Entity DB에 저장!
    @StepScope
    @Bean
    public JpaItemWriter<RestaurantMenu> menuFlatFileItemWriter(EntityManagerFactory entityManagerFactory) {

        JpaItemWriter<RestaurantMenu> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }


}
