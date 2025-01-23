package com.example.board.session;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SessionController {
	
    @GetMapping("/sessionTimer")
    @ResponseBody
    public int getSessionTimeLeft(HttpSession session) {
        int remainingTime = session.getMaxInactiveInterval() - (int) (System.currentTimeMillis() / 1000) + (int) (session.getCreationTime() / 1000);
        return remainingTime > 0 ? remainingTime : 0;
    }
    
 // 세션 연장 요청 (1시간으로 초기화)
    @PostMapping("/extendSession")
    @ResponseBody
    public ResponseEntity<?> extendSession(HttpSession session) {
        session.setMaxInactiveInterval(60 * 60);  // 1시간
        return ResponseEntity.ok(true);
    }

}
