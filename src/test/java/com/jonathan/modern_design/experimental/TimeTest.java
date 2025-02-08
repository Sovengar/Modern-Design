package com.jonathan.modern_design.experimental;

import com.jonathan.modern_design.config.TimeExtension;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TimeTest {
    @RegisterExtension
    TimeExtension timeExtension = new TimeExtension("2023-12-25");

    private static LocalDate testedCode() { // deep in a dark library
        return LocalDate.now();
    }

    @Test
    public void fixedTime() {
        LocalDate aDay = LocalDate.of(2023, 12, 25);
        assertThat(aDay).isEqualTo("2023-12-25");
    }

    @Test
    public void fixedTime2() {
        timeExtension.setDate(LocalDate.parse("2023-12-24"));
        LocalDate today = testedCode();
        assertThat(today).isAfter("2023-12-24");
    }
}
