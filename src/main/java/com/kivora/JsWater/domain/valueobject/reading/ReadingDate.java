package com.kivora.JsWater.domain.valueobject.reading;

import java.time.LocalDate;

public class ReadingDate {

    private final LocalDate value;

    public ReadingDate(LocalDate value) {
        this.value = value;
    }

    public static ReadingDate now() {
        return new ReadingDate(LocalDate.now());
    }

    public static ReadingDate of(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
        return new ReadingDate(value);
    }

    public LocalDate getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReadingDate that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
