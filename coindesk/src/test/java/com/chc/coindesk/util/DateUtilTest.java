package com.chc.coindesk.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class DateUtilTest {

    @Test
    public void testUpdateukDateToLocalDateTime() {
        // Initialize the date and format
        String inputDate = "10 December, 2023, 13:45:20";
        String inputFormat = "dd MMMM, yyyy, HH:mm:ss";

        // Execute the method under test
        String updatedDate = DateUtil.updateukDateToLocalDateTime(inputDate, inputFormat);

         // Assert that the output is as expected
        assertEquals("2023/12/10 13:45:20", updatedDate);
    }
}