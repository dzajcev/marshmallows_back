package com.dzaitsev.marshmallows.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ObjectMapperConfig {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Bean
    public ObjectMapper objectMapper() {
        LocalDateTimeDeserializer dateTimeDeserializer = new LocalDateTimeDeserializer(dateTimeFormatter);
        LocalDateTimeSerializer dateTimeSerializer = new LocalDateTimeSerializer(dateTimeFormatter);

        LocalDateDeserializer dateDeserializer = new LocalDateDeserializer(dateFormatter);
        LocalDateSerializer dateSerializer = new LocalDateSerializer(dateFormatter);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, dateTimeDeserializer);
        javaTimeModule.addSerializer(LocalDateTime.class, dateTimeSerializer);
        javaTimeModule.addDeserializer(LocalDate.class, dateDeserializer);
        javaTimeModule.addSerializer(LocalDate.class, dateSerializer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(javaTimeModule);
        return mapper;
    }

}
