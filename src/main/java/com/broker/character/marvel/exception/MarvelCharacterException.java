package com.broker.character.marvel.exception;

/**
 * MarvelCharacterException
 * @author prabhath amarasinghe
 */
public class MarvelCharacterException extends Exception {

    public MarvelCharacterException(String errorMessage) {
        super(errorMessage);
    }

    public MarvelCharacterException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public MarvelCharacterException(Throwable cause) {
        super(cause);
    }


}
