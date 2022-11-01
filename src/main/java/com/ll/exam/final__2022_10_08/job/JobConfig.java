package com.ll.exam.final__2022_10_08.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JobConfig {
    private final JobLauncher jobLauncher;
    private final Job makeRebateDataJob;

    // @Scheduled(cron = "0 0 4 * * *") // 실제 코드
    // @Scheduled(cron = "30 * * * * *") // 개발용 코드
    public void performMakeRebateDataJob() throws Exception {
        // String yearMonth = getPerformMakeRebateDataJobParam1Value(); // 실제 코드
        String yearMonth = "2022-11";

        JobParameters param = new JobParametersBuilder()
                .addString("yearMonth", yearMonth)
                .toJobParameters();
        JobExecution execution = jobLauncher.run(makeRebateDataJob, param);

        System.out.println(execution.getStatus());
    }

    public String getPerformMakeRebateDataJobParam1Value() {
        LocalDateTime localDateTime1 = LocalDateTime.now();
        LocalDateTime localDateTime2 = LocalDateTime.now().minusDays(14);

        LocalDateTime localDateTime3 = localDateTime1.getMonth() == localDateTime2.getMonth() ? LocalDateTime.now().minusMonths(1) : LocalDateTime.now().minusMonths(2);

        return  "%04d".formatted(localDateTime3.getYear()) + "-" + "%02d".formatted(localDateTime3.getMonthValue());
    }
}
