package com.example.board.likes.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikesDao {

	public boolean existsLike(Map<String, Object> map);

	public void deleteLike(Map<String, Object> map);

	public void insertLike(Map<String, Object> map);

	public int countLikes(Long boardId);

}
