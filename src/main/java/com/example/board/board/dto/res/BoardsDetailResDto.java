package com.example.board.board.dto.res;


import java.time.LocalDateTime;
import java.util.List;

import com.example.board.board.domain.Comments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardsDetailResDto {

	private long board_id;
	private String title;
	private String content;
	private String username;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private List<Comments> comments;
	private Long liked_count;
	private Long liked;
}
