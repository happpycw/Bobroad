package com.grepp.synapse4.app.model.user.entity;

import com.grepp.synapse4.app.model.auth.code.Role;
import com.grepp.synapse4.app.model.auth.code.Provider;
import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String userAccount;

    @Column
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isSurvey = false;

    @ColumnDefault("true")
    private Boolean activated = true;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Enumerated(EnumType.STRING)
    @Column
    private Provider provider; // 계정 출처 구분(ex_local, google 등)

    @Column
    private String providerId;  // 소셜 로그인의 고유 사용자 id 값


    @Builder
    public User(String userAccount, String password, String name, String nickname, String email,
        Boolean isSurvey, Boolean activated, LocalDateTime deletedAt, Role role, Provider provider,
        String providerId) {
        this.userAccount = userAccount;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.isSurvey = isSurvey;
        this.activated = activated;
        this.deletedAt = deletedAt;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public User(String userAccount, String name, String email) {
        this.userAccount = userAccount;
        this.name = name;
        this.email = email;
    }
}
