package com.grepp.synapse4.app.model.llm;

import com.grepp.synapse4.app.model.llm.mongo.RestaurantTagsDocument;
import com.grepp.synapse4.app.model.llm.repository.RestaurantTagsDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantCandidateFromMongoService {

    private final RestaurantTagsDocumentRepository restaurantTagRepository;

    public List<RestaurantTagsDocument> findRestaurantsByTags(List<String> tags) {
        return restaurantTagRepository.findByTagIn(tags);
    }
}
