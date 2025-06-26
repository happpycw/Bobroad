package com.grepp.synapse4.app.model.llm.repository;

import com.grepp.synapse4.app.model.llm.mongo.RestaurantTagsDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRestaurantTagsRepositoryImpl implements CustomRestaurantTagsRepository{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<String> findDistinctTag() {
        // MongoDB의 tag 필드의 값들을 중복없이 get
        return mongoTemplate.query(RestaurantTagsDocument.class)
                .distinct("tag")
                .as(String.class)
                .all();
    }
}