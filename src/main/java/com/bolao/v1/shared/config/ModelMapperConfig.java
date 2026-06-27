package com.bolao.v1.shared.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelMapperConfig {


    @Bean
    public ModelMapper modelMapper () {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setAmbiguityIgnored(true);
        return mapper;


    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
