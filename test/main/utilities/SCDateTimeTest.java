package main.utilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SCDateTimeTest {
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getDateTime() {
        long curTime = 1495911844611L;
        String formatted = SCDateTime.getDateTime(curTime);
        assertTrue(formatted.equals("2017-05-27 PDT 12:04:04.611"));
    }

}