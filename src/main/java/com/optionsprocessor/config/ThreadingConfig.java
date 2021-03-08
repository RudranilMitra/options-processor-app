package com.optionsprocessor.config;

import com.optionsprocessor.constants.Symbols;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class ThreadingConfig {
    @Value("${threading.num_threads}")
    private Integer numThreads;
    HashMap<Integer, List<String>> threadMap = new HashMap<Integer, List<String>>();

    @Bean
    public HashMap<Integer, List<String>> getThreadMap() {
        Integer loopIndex = 1;
        for (String symbol : Symbols.getOptionSymbols()) {
            int threadNum = loopIndex % numThreads;
            threadNum += 1;
            if (threadMap.get(threadNum) == null) {
                List symbolList = new LinkedList<String>();
                symbolList.add(symbol);
                threadMap.put(threadNum, symbolList);
            } else {
                threadMap.get(threadNum).add(symbol);
            }
            loopIndex += 1;
        }
        return threadMap;
    }

    @Bean
    public Integer getNumThreads() {
        return numThreads;
    }
}
