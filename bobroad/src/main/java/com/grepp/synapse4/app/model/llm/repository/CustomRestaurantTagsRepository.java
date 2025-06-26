package com.grepp.synapse4.app.model.llm.repository;

import java.util.List;

public interface CustomRestaurantTagsRepository {

    List<String> findDistinctTag();
}
