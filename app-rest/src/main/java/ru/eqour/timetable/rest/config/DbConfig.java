package ru.eqour.timetable.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.eqour.timetable.db.MongoDbClient;

@Configuration
public class DbConfig {

    @Value("${db.connection}")
    private String connectionString;

    @Bean
    public MongoDbClient mongoDbClient() {
        return new MongoDbClient(connectionString);
    }
}
