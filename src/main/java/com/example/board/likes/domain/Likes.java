package com.example.board.likes.domain;

import com.example.board.board.domain.Boards;
import com.example.board.users.domain.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "board_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Boards boards;
}