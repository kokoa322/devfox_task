package com.example.board.likes.service;

public interface LikesService {

	boolean toggleLike(Long board_id, Long user_id);

	int getLikeCount(Long boardId);

}
