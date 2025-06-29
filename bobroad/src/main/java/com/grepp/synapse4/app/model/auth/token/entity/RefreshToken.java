package com.grepp.synapse4.app.model.auth.token.entity;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@RedisHash(value = "refreshToken", timeToLive = 3600 * 24 * 7)
public class RefreshToken {
    @Id
    private String id = UUID.randomUUID().toString();
    private String email;
    @Indexed
    private String accessTokenId;
    private String token = UUID.randomUUID().toString();
    
    public RefreshToken() {
    }
    
    public RefreshToken(String email, String id) {
        this.email = email;
        this.accessTokenId = id;
    }
}
