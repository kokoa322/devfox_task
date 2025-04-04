package com.example.board.likes.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.board.aspect.RequireSession;
import com.example.board.likes.service.LikesService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LikesController {
	
	private final LikesService likesService;
	
	@RequireSession
	@ResponseBody
	@PostMapping("/likes")
	public ResponseEntity<?> toggleLike(
	        @RequestParam("board_id") Long board_id,
	        @RequestParam(value = "user_id", required = false) Long user_id ,
	        HttpSession httpSession) {
		
	    boolean result = likesService.toggleLike(board_id, user_id);
	    
	    return ResponseEntity.ok(result);
	}

}
