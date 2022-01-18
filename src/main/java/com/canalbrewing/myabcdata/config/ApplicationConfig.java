package com.canalbrewing.myabcdata.config;

import com.canalbrewing.myabcdata.resultsetmapper.ResultSetMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ResultSetMapper reseultSetMapper() {
        return new ResultSetMapper();
    }

}
