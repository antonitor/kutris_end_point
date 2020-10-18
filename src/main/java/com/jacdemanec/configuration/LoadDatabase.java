package com.jacdemanec.configuration;

import com.jacdemanec.model.AppPlayer;
import com.jacdemanec.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PlayerRepository repository) {

        LocalDateTime dateTime = LocalDateTime.parse("2020-01-01T08:22:12");
        return args -> {
            //log.info("Preloading " + repository.save(new AppPlayer("PEPE", "", 0, 0, 0 ,1,dateTime)));
        };
    }
}
