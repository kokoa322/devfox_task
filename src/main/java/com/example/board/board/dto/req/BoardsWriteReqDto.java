package com.example.board.board.dto.req;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 投稿作成リクエストDTOクラス。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardsWriteReqDto {

	private String username;
	private String title;
	private String content;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
}
