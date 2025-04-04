package com.example.board.likes.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.board.likes.dao.LikesDao;
import com.example.board.likes.service.LikesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService{
	
	private final LikesDao likesDao;

	@Override
	public boolean toggleLike(Long board_id, Long user_id) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("board_id", board_id);
		map.put("user_id", user_id);
		
		// 1. 현재 좋아요 여부 확인
	    boolean exists = likesDao.existsLike(map);

	    if (exists) {
	        // 2. 이미 좋아요한 상태 → 좋아요 취소
	    	likesDao.deleteLike(map);
	        return false; // 좋아요 제거 상태 반환
	    } else {
	        // 3. 좋아요하지 않은 상태 → 좋아요 추가
	    	likesDao.insertLike(map);
	        return true; // 좋아요 추가 상태 반환
	    }
	}

	@Override
	public int getLikeCount(Long boardId) {
	    return likesDao.countLikes(boardId);
	}
	

}
