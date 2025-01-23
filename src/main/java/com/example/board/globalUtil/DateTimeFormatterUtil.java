package com.example.board.globalUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateTimeFormatterUtil {

    // 현재 시간을 ISO_LOCAL_DATE_TIME 포맷으로 반환하는 메소드
    public String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
