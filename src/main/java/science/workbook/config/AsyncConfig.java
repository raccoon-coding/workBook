package science.workbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {
    @Bean(name = "refresh token save Async")
    public Executor gptGptRiskExecutor() {
        ThreadPoolTaskExecutor poolExecutor = new ThreadPoolTaskExecutor();
        poolExecutor.setCorePoolSize(20);
        poolExecutor.setMaxPoolSize(50);
        poolExecutor.setQueueCapacity(50);
        poolExecutor.setThreadNamePrefix("Refresh token Save -");
        poolExecutor.initialize();

        return poolExecutor;
    }
}
