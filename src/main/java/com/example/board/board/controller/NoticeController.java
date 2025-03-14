package com.example.board.board.controller;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.board.board.service.SseEmitterService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/sse")
@RequiredArgsConstructor
public class NoticeController {
	private final SseEmitterService sseEmitterService;

	@GetMapping("/subscribe/{userId}")
	public SseEmitter subscribe(@PathVariable Long userId) {
	    SseEmitter emitter = new SseEmitter();
	    try {
	        // SSE 이벤트 전송
	        emitter.send(SseEmitter.event().name("message").data("Hello User " + userId));
	    } catch (IOException e) {
	        emitter.completeWithError(e);  // 예외 발생 시 처리
	    }
	    return emitter;
	}
}

