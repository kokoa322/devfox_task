package com.example.board.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.example.board.likes.domain.Likes;
import com.example.board.users.domain.Users;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boards")
public class Boards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "hits", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Long hits;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime created_at;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "boards", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments;

    @PrePersist
    public void setCreated_at() {
        this.created_at = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdated_at() {
        this.updated_at = LocalDateTime.now();
    }
    
    @OneToMany(mappedBy = "boards", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes;
    

}
