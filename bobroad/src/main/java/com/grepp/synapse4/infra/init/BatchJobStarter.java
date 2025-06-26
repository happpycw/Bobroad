package com.grepp.synapse4.infra.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class BatchJobStarter implements CommandLineRunner {     //CommandLineRunner : 서버 실행 시 배치를 실행하게 하는 인터페이스

    private final JobLauncher jobLauncher;
    private final Job fileReadWriteJob;         // FileReaderJobConfig 의 job과 같은 이름이어야, 이 job이 실행됨

    @Override
    public void run(String... args) throws Exception {
        log.info("🖐️🖐️🖐️ Batch Job start!!!");

        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("timestamp", System.currentTimeMillis())    //매번 실행되는
                .addString("version", "1.0")            //같은 이름의 job은 실행되지 않는 현상을 이용한 것!
                .toJobParameters();

        jobLauncher.run(fileReadWriteJob, jobParameters);
    }


}
