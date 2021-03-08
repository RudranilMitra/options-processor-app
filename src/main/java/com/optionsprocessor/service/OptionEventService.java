package com.optionsprocessor.service;

import com.optionsprocessor.helper.OptionEventTransformer;
import com.optionsprocessor.model.OptionEvent;
import lombok.SneakyThrows;
import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.List;

public class OptionEventService implements Runnable{
    private String accessToken;
    private String optionUrl;
    private HttpClient httpClient;
    private List<String> optionSymbol;
    private KafkaTemplate<String, OptionEvent> kafkaTemplate;

    Logger logger = LoggerFactory.getLogger(OptionEventService.class);

    public OptionEventService(String accessToken, List<String> optionSymbol, KafkaTemplate<String, OptionEvent> kafkaTemplate) {
        this.accessToken = "Bearer ".concat(accessToken);
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        this.optionSymbol = optionSymbol;
        this.kafkaTemplate = kafkaTemplate;
    }

    @SneakyThrows
    public void postOptionEvent() {
        for (String symbol : optionSymbol) {
            optionUrl = "https://api.tdameritrade.com/v1/marketdata/chains?apikey=FOCHMVCKOEYMWELYSHVE4SESRHZBSVUG&symbol=".concat(symbol);
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(optionUrl))
                    .setHeader("Authorization", accessToken)
                    .build();
            logger.info("Fetching option stream for: ".concat(optionUrl));
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());



            OptionEvent optionEvent = OptionEventTransformer.stringToJsonObject(response.body());

            ListenableFuture<SendResult<String, OptionEvent>> future =
                    kafkaTemplate.send("option_events_inbound", symbol, optionEvent);

            future.addCallback(new ListenableFutureCallback<SendResult<String, OptionEvent>>() {
                @Override
                public void onSuccess(SendResult<String, OptionEvent> result) {
                    logger.info("Sent message to topic with key: ".concat(symbol).concat(" offset:").concat(String.valueOf(result.getRecordMetadata().offset())));
                }

                @Override
                public void onFailure(Throwable ex) {
                    logger.error("Unable to send message to topic with key: ".concat(symbol));
                }
            });
        }
    }

    public void run(){
        postOptionEvent();
    }
}
