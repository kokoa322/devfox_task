package com.example.board.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.board.board.domain.Boards;

@Repository
public interface BoardsRepository extends JpaRepository<Boards, Long>{
	
	Page<Boards> findAllByOrderByIdDesc(Pageable pageable);

}
