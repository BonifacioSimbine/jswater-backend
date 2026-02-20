package com.kivora.JsWater.domain.valueobject.reading;

import java.util.UUID;

public record ReadingId (UUID value){

    public ReadingId {
        if (value==null){
            throw new NullPointerException("O ID nao pode ser nulo.");
        }
    }

    public static ReadingId generate(){
        return new ReadingId(UUID.randomUUID());
    }

    public static ReadingId from(String value){
        return new ReadingId(UUID.fromString(value));
    }

    @Override
    public String toString(){
        return value.toString();
    }
}
