package com.example.board.board.dto.req;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardsEditReqDto {

	private long id;
	private String username;
	private String title;
	private String content;
}
