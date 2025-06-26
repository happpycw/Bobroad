package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.user.dto.MyBookMarkDto;
import com.grepp.synapse4.app.model.user.repository.BookMarkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookMarkRepository bookMarkRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    @Test
    void findMyBookMarkDto() throws Exception {
        // given
        Long userId = 1L;

        List<MyBookMarkDto> mockResult = List.of(
                new MyBookMarkDto(
                        userId, 100L, LocalDateTime.now(), 10L,
                        "식당A", "서울시 강남구", "본점", "한식", "09:00~21:00"
                ),
                new MyBookMarkDto(
                        userId, 101L, LocalDateTime.now(), 11L,
                        "식당B", "부산시 해운대구", "2호점", "중식", "10:00~22:00"
                )
        );

        given(bookMarkRepository.findmybookmark(userId)).willReturn(mockResult);

        // when
        List<MyBookMarkDto> result = bookmarkService.findByBookmarkId(userId);

        // then
        System.out.println("조회된 북마크 수: " + result.size());
        for (MyBookMarkDto dto : result) {
            System.out.println(dto.getRestaurantName() + " | " + dto.getCategory() + " | " + dto.getRestaurantAddress());
        }

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRestaurantName()).isEqualTo("식당A");
        assertThat(result.get(1).getCategory()).isEqualTo("중식");

    }
}