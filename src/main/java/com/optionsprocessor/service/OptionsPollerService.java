package com.optionsprocessor.service;

import com.optionsprocessor.authenticator.AccessTokenService;
import com.optionsprocessor.config.ThreadingConfig;
import com.optionsprocessor.model.OptionEvent;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class OptionsPollerService {

    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private KafkaTemplate<String, OptionEvent> kafkaTemplate;
    @Autowired
    private ThreadingConfig threadingConfig;
    @Value("${app.topic}")
    private String outboundTopicName;

    Logger logger = LoggerFactory.getLogger(OptionsPollerService.class);

    @Scheduled(cron = "0/5 * * * * ?")
    @SneakyThrows
    public void create() {
        String accessToken = accessTokenService.generateAccessToken();
        ExecutorService executorService = Executors.newFixedThreadPool(threadingConfig.getNumThreads());
        for (int threadNum = 1; threadNum <= threadingConfig.getThreadMap().size(); threadNum++) {
            logger.info("Start executing thread : ".concat(String.valueOf(threadNum)));
            Runnable workerThread = new OptionEventService(accessToken,
                    threadingConfig.getThreadMap().get(threadNum),
                    kafkaTemplate,
                    outboundTopicName);
            executorService.execute(workerThread);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        logger.info("All threads complete");
    }
}