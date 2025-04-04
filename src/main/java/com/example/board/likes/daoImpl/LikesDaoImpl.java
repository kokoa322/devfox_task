package com.example.board.likes.daoImpl;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.board.likes.dao.LikesDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LikesDaoImpl implements LikesDao{

	private final SqlSessionTemplate sqlSession;

	@Override
	public boolean existsLike(Map<String, Object> map) {
		
		Integer result = sqlSession.selectOne("Likes.exists", map);
	    return result != null && result > 0;
	}

	@Override
	public void deleteLike(Map<String, Object> map) {
		sqlSession.delete("Likes.delete", map);
		
	}

	@Override
	public void insertLike(Map<String, Object> map) {
		sqlSession.insert("Likes.save", map);
		
	}

	@Override
	public int countLikes(Long boardId) {
		return sqlSession.selectOne("Likes.countByBoardId", boardId);
	}
	
	
}
