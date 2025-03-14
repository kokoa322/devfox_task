package com.example.board.board.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Service
public class SseEmitterService {

    
    private final Map<Long, Integer> alarmCounts = new HashMap<>(); // 알림 개수 관리
   
    private final Map<Long, SseEmitter> emitters = new HashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(30));
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(userId);
        });

        emitter.onTimeout(() -> {
            emitters.remove(userId);
        });

        return emitter;
    }

    public void sendAlarm (Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                // 알림 개수 증가
                alarmCounts.put(userId, alarmCounts.getOrDefault(userId, 0) + 1);

                // 메시지 전송
                emitter.send(message);
            } catch (Exception e) {
                emitters.remove(userId);
                System.err.println("SSE 메시지 전송 실패: " + e.getMessage());
            }
        } else {
            System.err.println("SSE 연결 없음: 사용자 ID " + userId);
        }
    }

}


