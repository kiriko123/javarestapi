package com.example.shopapp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "social_accounts")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    private String email;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
