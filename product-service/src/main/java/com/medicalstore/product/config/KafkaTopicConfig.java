package com.medicalstore.product.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {


    /**
     * Topic for new medicine creation event
     */
    @Bean
    public NewTopic medicineCreatedTopic() {

        return TopicBuilder
                .name("medicine-created")
                .partitions(3)
                .replicas(1)
                .build();
    }


    /**
     * Topic for medicine update event
     */
    @Bean
    public NewTopic medicineUpdatedTopic() {

        return TopicBuilder
                .name("medicine-updated")
                .partitions(3)
                .replicas(1)
                .build();
    }


    /**
     * Topic for medicine deletion event
     */
    @Bean
    public NewTopic medicineDeletedTopic() {

        return TopicBuilder
                .name("medicine-deleted")
                .partitions(3)
                .replicas(1)
                .build();
    }
}