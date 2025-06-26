package com.grepp.synapse4.app.model.restaurant;

import com.grepp.synapse4.app.model.restaurant.dto.KakaoSearchRequestDto;
import com.grepp.synapse4.app.model.restaurant.dto.KakaoSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoSearchService {

    private final WebClient kakaoWebClient;

    public Optional<KakaoSearchResponseDto.KakaoPlace> search(KakaoSearchRequestDto request) {
        KakaoSearchResponseDto response = kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", request.getQuery())
                        .queryParam("x", request.getLongitude())
                        .queryParam("y", request.getLatitude())
                        .queryParam("category_group_code", "FD6")
                        .queryParam("radius", 1000)
                        .queryParam("size", 15)
                        .build())
                .retrieve()
                .bodyToMono(KakaoSearchResponseDto.class)
                .block();

        if (ObjectUtils.isEmpty(response) || response.getDocuments() == null || response.getDocuments().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(response.getDocuments().get(0));
    }
}
