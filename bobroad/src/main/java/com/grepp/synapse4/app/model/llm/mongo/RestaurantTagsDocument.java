package com.grepp.synapse4.app.model.llm.mongo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "restaurant_tags")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTagsDocument {

    @Id
    private String id;      // mongo id
    private Long restaurantId;
    private List<String> tag;
    private String saurceText;
}
