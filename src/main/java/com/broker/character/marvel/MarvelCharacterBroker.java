package com.broker.character.marvel;

import com.broker.character.marvel.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication ( scanBasePackageClasses = {AppConfig.class} )
public class MarvelCharacterBroker {

    public static void main(String[] args) {
        SpringApplication.run(MarvelCharacterBroker.class, args);
    }


}
