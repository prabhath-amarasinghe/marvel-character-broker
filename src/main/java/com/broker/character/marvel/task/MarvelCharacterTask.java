package com.broker.character.marvel.task;

import com.broker.character.marvel.config.AppConfig;
import com.broker.character.marvel.exception.MarvelCharacterException;
import com.broker.character.marvel.model.Data;
import com.broker.character.marvel.model.MarvelCharacter;
import com.broker.character.marvel.model.MarvelResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class MarvelCharacterTask implements Callable <Map<Long, MarvelCharacter>> {

    public static final Logger LOGGER = LogManager.getLogger(MarvelCharacterTask.class);

    private RestTemplate restTemplate;

    private String marvelPrivateApiKey;

    private String marvelPublicApiKey;

    private final Integer start;

    private final Integer target;


    public MarvelCharacterTask(RestTemplate restTemplate, Integer start, Integer target, String marvelPrivateApiKey, String marvelPublicApiKey) {
        this.restTemplate = restTemplate;
        this.start = start;
        this.target = target;
        this.marvelPrivateApiKey = marvelPrivateApiKey;
        this.marvelPublicApiKey = marvelPublicApiKey;
    }

    private Data getMarvelResponseData(int offset) throws MarvelCharacterException {
        StringBuilder url = buildUrl(offset);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, buildDefaultHeader());

        try {
            ResponseEntity<MarvelResponse> response = performRequest(url, HttpMethod.GET, httpEntity, MarvelResponse.class);

            if (response == null || response.getBody() == null) {
                LOGGER.error("No body returned in response after issuing the request to Marvel Api");
                throw new MarvelCharacterException("No body returned in response after issuing the request to Marvel Api");
            } else {
                return response.getBody().getData();
            }
        } catch (RestClientException rex) {
            LOGGER.error("Unexpected exception when retrieving Marvel Characters using Marvel Api");
            throw new MarvelCharacterException("Unexpected exception when retrieving Marvel Characters using Marvel Api");
        }
    }

    private StringBuilder buildUrl(int offset) throws MarvelCharacterException {
        String timeStamp =UUID.randomUUID().toString().replace("-", "");
        return new StringBuilder()
                .append(AppConfig.MARVEL_COMMON_ENDPOINT)
                .append(AppConfig.MARVEL_CHARACTER_ENDPOINT)
                .append("?").append(AppConfig.MARVEL_OFFSET_PARAM).append(offset)
                .append("&").append(AppConfig.MARVEL_TIMESTAMP_PARAM).append(timeStamp)
                .append("&").append(AppConfig.MARVEL_API_KEY_PARAM).append(marvelPublicApiKey)
                .append("&").append(AppConfig.MARVEL_HASH_PARAM).append(buildHash(timeStamp, marvelPrivateApiKey, marvelPublicApiKey));
    }

    private HttpHeaders buildDefaultHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        List<MediaType> acceptTypes = new ArrayList<>();
        acceptTypes.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptTypes);

        return httpHeaders;
    }

    private String buildHash(String timeStamp, String marvelApiPrivateKey, String marvelApiKey) throws MarvelCharacterException {
        byte[] digest;
        String keyCombination = new StringBuilder()
                .append(timeStamp)
                .append(marvelApiPrivateKey)
                .append(marvelApiKey).toString();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytesOfHashString = keyCombination.getBytes(StandardCharsets.UTF_8);
            digest = md.digest(bytesOfHashString);

        } catch (NoSuchAlgorithmException ex) {
            throw new MarvelCharacterException(ex.getMessage(), ex);
        }

        if (digest.length == 0) {
            throw new MarvelCharacterException("Failed to build the hash parameter");
        }

        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }

    private <T extends MarvelResponse> ResponseEntity<T> performRequest(StringBuilder builder, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> returnType) {
        String url = builder.toString();
        LOGGER.debug("Issuing request to API: method=[{}], url=[{}]", httpMethod, url);
        ResponseEntity<T> response = restTemplate.exchange(url, httpMethod, httpEntity, returnType);

        return response;
    }

    @Override
    public Map<Long, MarvelCharacter> call() throws MarvelCharacterException  {
        int total = 0;
        int count = 0;
        int index = start;

        Data data;
        Map<Long, MarvelCharacter> result = new HashMap<>();

        LOGGER.info("Retrieving all Marvel characters");

        while (index < target) {
            data = getMarvelResponseData(index);

            result.putAll(data.getResults().stream().collect(Collectors.toMap(MarvelCharacter::getId, character -> character)));

            total = data.getTotal();
            count = data.getCount();
            index = index + count;

            if (index >= total) {
                break;
            }

        }
        return result;
    }
}
