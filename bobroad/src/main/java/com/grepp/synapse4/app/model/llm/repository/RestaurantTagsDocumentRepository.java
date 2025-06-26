package com.grepp.synapse4.app.model.llm.repository;

import com.grepp.synapse4.app.model.llm.mongo.RestaurantTagsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RestaurantTagsDocumentRepository extends MongoRepository<RestaurantTagsDocument, String>, CustomRestaurantTagsRepository{

    List<RestaurantTagsDocument> findByTagIn(List<String> tags);
}
