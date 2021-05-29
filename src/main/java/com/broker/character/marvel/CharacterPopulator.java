package com.broker.character.marvel;

import com.broker.character.marvel.exception.MarvelCharacterException;
import com.broker.character.marvel.model.MarvelCharacter;
import com.broker.character.marvel.task.MarvelCharacterTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class CharacterPopulator {

    public static final Logger LOGGER = LogManager.getLogger(CharacterPopulator.class);

    private static final Integer THREAD_POOL_SIZE = 8;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<Long, com.character.marvel.model.MarvelCharacter> kafkaTemplate;

    @Value("${marvel.api.private.key}")
    private String marvelPrivateApiKey;

    @Value("${marvel.api.public.key}")
    private String marvelPublicApiKey;

    @Value("${kafka.marvel.character.topic}")
    private String marvelCharacterTopic;

    private final Map<Long, MarvelCharacter> characterMap = new HashMap<>();

    private  final List<Long> characterIdList = new ArrayList<>();
    private final Map<Integer, Long> readItems = new HashMap<>();



    @PostConstruct
    private void populateCharacters() throws MarvelCharacterException, InterruptedException, ExecutionException {

        LOGGER.info("Populating Characters");
        List<Callable<Map<Long, MarvelCharacter>>> taskList = new ArrayList<>();

        taskList.add(new MarvelCharacterTask(restTemplate, 0, 200, marvelPrivateApiKey, marvelPublicApiKey));
        taskList.add(new MarvelCharacterTask(restTemplate, 200, 400, marvelPrivateApiKey, marvelPublicApiKey));
        taskList.add(new MarvelCharacterTask(restTemplate, 400, 600, marvelPrivateApiKey, marvelPublicApiKey));
        taskList.add(new MarvelCharacterTask(restTemplate, 600, 800, marvelPrivateApiKey, marvelPublicApiKey));
        taskList.add(new MarvelCharacterTask(restTemplate, 800, 1000, marvelPrivateApiKey, marvelPublicApiKey));
        taskList.add(new MarvelCharacterTask(restTemplate, 1000, 1200, marvelPrivateApiKey, marvelPublicApiKey));
        taskList.add(new MarvelCharacterTask(restTemplate, 1200, 1400, marvelPrivateApiKey, marvelPublicApiKey));
        taskList.add(new MarvelCharacterTask(restTemplate, 1400, 1600, marvelPrivateApiKey, marvelPublicApiKey));

        ExecutorService service  = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        List<Future<Map<Long, MarvelCharacter>>> futures = service.invokeAll(taskList);

        for(Future<Map<Long, MarvelCharacter>> future: futures) {
            characterMap.putAll(future.get());
        }

        service.shutdown();

        characterIdList.addAll(characterMap.entrySet().stream().map(item -> item.getKey()).collect(Collectors.toList()));

        LOGGER.info("[{}] Characters populated", characterIdList.size());
    }

    public Map<Long, MarvelCharacter> getCharacterMap() {
        return characterMap;
    }


    @Scheduled(fixedDelay = 2000, initialDelay = 1000)
    public void fixedDelaySch() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, characterIdList.size());

        if (readItems.get(randomNum) == null) {
            MarvelCharacter character = characterMap.get(characterIdList.get(randomNum));

            LOGGER.info("Emitting Character [{}]", character);
            kafkaTemplate.send(marvelCharacterTopic, character.getId(), buildAvroMessage(character));

            readItems.put(randomNum, character.getId());

            LOGGER.info("[{}] of [{}] Emitted", readItems.size(), characterIdList.size());
        }
    }

    private com.character.marvel.model.MarvelCharacter buildAvroMessage(MarvelCharacter marvelCharacter) {

        com.character.marvel.model.Thumbnail thumbnail = null;
        if (marvelCharacter.getThumbnail() != null) {
            thumbnail = com.character.marvel.model.Thumbnail.newBuilder()
                    .setPath(marvelCharacter.getThumbnail().getPath() == null ? "" : marvelCharacter.getThumbnail().getPath())
                    .setExtension(marvelCharacter.getThumbnail().getExtension() == null ? "" : marvelCharacter.getThumbnail().getExtension())
                    .build();
        }

        return com.character.marvel.model.MarvelCharacter.newBuilder()
            .setId(marvelCharacter.getId())
            .setName(marvelCharacter.getName() == null ? null : marvelCharacter.getName())
            .setDescription(marvelCharacter.getDescription() == null ? null : marvelCharacter.getDescription())
            .setThumbnail(thumbnail)
            .build();
    }
}
