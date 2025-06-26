package com.grepp.synapse4.app.model.user.dto;

import com.grepp.synapse4.app.model.user.entity.Bookmark;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString

public class BookMarkDto {

    private Long userId;
    private Long bookMarkId;
    private Long restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private LocalDateTime createdAt;

    public static BookMarkDto fromEntity(Bookmark b) {
        BookMarkDto dto = new BookMarkDto();
        dto.setBookMarkId(b.getId());
        dto.setRestaurantId(b.getRestaurant().getId());
        dto.setRestaurantName(b.getRestaurant().getName());
        dto.setRestaurantAddress(b.getRestaurant().getAddress());
        dto.setUserId(b.getId());
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }


}
