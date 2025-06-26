package com.grepp.synapse4.app.model.restaurant.repository;

import com.grepp.synapse4.app.model.restaurant.KakaoSearchService;
import com.grepp.synapse4.app.model.restaurant.dto.KakaoSearchRequestDto;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoRestaurantVerifierService {

    // 외부 api와 소통하니, Service 레이어에서 처리
    private final RestaurantRepository restaurantRepository;
    private final KakaoSearchService kakaoSearchService;

    @Transactional
    public void verifyAndActivateRestaurant(){
        List<Restaurant> restaurants = restaurantRepository.findByActivatedFalse();

        for (Restaurant restaurant : restaurants) {

            System.out.println("🔍 검증 대상: " + restaurant.getName());
            KakaoSearchRequestDto requestDto = new KakaoSearchRequestDto(
                    restaurant.getName(),
                    restaurant.getLatitude(),
                    restaurant.getLongitude()
//                    restaurant.getDetail().getHomePageURL()
            );

            kakaoSearchService.search(requestDto).ifPresent(kakaoPlace -> {
                if (isSimilar(restaurant.getName(), kakaoPlace.getPlace_name())){
                    restaurant.setActivated(true);
                    restaurant.setKakaoId(kakaoPlace.getId());
//                    restaurant.getDetail().setHomePageURL(kakaoPlace.getPlace_url());
                }
            });
        }
    }

    // todo 띄어쓰기, 지점명 등 고도화 필요... 특히 강승월한식당...
    private boolean isSimilar(String name, String placeName) {
        if(name.contains(placeName) || placeName.contains(name)){
            System.out.println("✅ 검증 통과! DBname: " + name + ", KakaoplaceName: " + placeName);
            return true;
        }
        else return false;
    }

}
