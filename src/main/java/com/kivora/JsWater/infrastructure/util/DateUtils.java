package com.kivora.JsWater.infrastructure.util;

import java.time.LocalDate;
import java.time.YearMonth;

public class DateUtils {

    public static LocalDate toLocalDate(YearMonth ym) {
        return ym.atDay(1);
    }

    public static YearMonth toYearMonth(LocalDate date) {
        return YearMonth.from(date);
    }
}

