package com.grepp.synapse4.app.model.restaurant;

import com.grepp.synapse4.app.model.restaurant.dto.search.SearchRestaurantResponseDto;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantSearchServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantSearchService restaurantSearchService;

    @Test
    void searchRestaurant() {
        // given
        String keyword = "김밥";

        Restaurant mockRestaurant = mock(Restaurant.class);
        when(restaurantRepository.findByNameContainingAndActivatedIsTrue(keyword))
                .thenReturn(List.of(mockRestaurant));

        SearchRestaurantResponseDto mockDto = mock(SearchRestaurantResponseDto.class);
        when(mockDto.getName()).thenReturn("김밥천국");

        try (MockedStatic<SearchRestaurantResponseDto> mockedStatic = mockStatic(SearchRestaurantResponseDto.class)) {
            mockedStatic.when(() -> SearchRestaurantResponseDto.fromEntity(mockRestaurant))
                    .thenReturn(mockDto);

            // when
            List<SearchRestaurantResponseDto> result = restaurantSearchService.searchByName(keyword);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("김밥천국");

            //출력
            result.forEach(dto -> System.out.println("식당 이름: " + dto.getName()));
        }
    }
}