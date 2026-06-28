package ru.ural.notifications.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperty {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private String groupId;

    private Topic notificationEmailTopic;

    private Topic notificationContractTopic;

    @Setter
    @Getter
    public static class Topic {

        private String name;

        private Integer partitions;

        private Short replicationFactor;

    }

}
