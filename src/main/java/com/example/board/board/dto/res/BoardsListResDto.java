package com.example.board.board.dto.res;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 投稿リストのレスポンスDTOクラス。
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardsListResDto {

    private Long id;
    private String username;
    private String title;
    private LocalDateTime created_at;
    private String formattedCreated_at;

    public BoardsListResDto(Long id, String username, String title, LocalDateTime created_at) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.created_at = created_at;

        if (created_at != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.formattedCreated_at = created_at.format(formatter);
        }
    }
}
