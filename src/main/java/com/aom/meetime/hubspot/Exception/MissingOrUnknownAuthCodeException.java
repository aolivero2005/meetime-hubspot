package com.aom.meetime.hubspot.Exception;

public class MissingOrUnknownAuthCodeException extends RuntimeException {

    public MissingOrUnknownAuthCodeException(String message){
        super(message);
    }


}
