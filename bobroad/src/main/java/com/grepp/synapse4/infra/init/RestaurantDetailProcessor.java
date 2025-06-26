package com.grepp.synapse4.infra.init;

import com.grepp.synapse4.app.model.restaurant.dto.create.CsvRestaurantDetailDto;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.entity.RestaurantDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Component
@StepScope
@Slf4j
public class RestaurantDetailProcessor implements ItemProcessor<CsvRestaurantDetailDto, RestaurantDetail> {

    private final Map<String, Restaurant> preLoadedRestaurantsMap;

    public RestaurantDetailProcessor(Map<String, Restaurant> preLoadedRestaurantsMap) {
        this.preLoadedRestaurantsMap = preLoadedRestaurantsMap;
    }

    private int count=0;

    @Override
    @Transactional(readOnly = true)
    public RestaurantDetail process(CsvRestaurantDetailDto dto) throws Exception {
        Restaurant restaurant = preLoadedRestaurantsMap.get(dto.getPublicId());

        count++;
        if (count % 10000 == 0) {
            log.info("🍴 Processed {} 개 식당 상세 정보를 탐색합니다.", count);
        }

        if (ObjectUtils.isEmpty(restaurant)) {
            log.debug("Restaurant not found for publicId: {}", dto.getPublicId());
            return null;
        }

        if (dto.getDayOff().length() > 255){
            log.warn("dayOff Data is too long, publicId: {}", dto.getPublicId());
            return null;
        }

        RestaurantDetail detail = RestaurantDetail.builder()
                .restaurant(restaurant)
                .dayOff(dto.getDayOff())
                .rowBusinessTime(dto.getRowBusinessTime())
                .parking(dto.getParking())
                .wifi(dto.getWifi())
                .delivery(dto.getDelivery())
                .homePageURL(dto.getHomePageURL())
                .build();

        detail.setId(restaurant.getId());

        return detail;
    }
}