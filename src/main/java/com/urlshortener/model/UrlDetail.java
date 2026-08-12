package com.urlshortener.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "url_details")
@Data
public class UrlDetail implements Serializable {

    @Column
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false)
    private String longUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime expiresAt;

    @Column
    private int clickCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
