package com.grepp.synapse4.infra.llm.config;

import com.mongodb.client.MongoClient;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.mongodb.IndexMapping;
import dev.langchain4j.store.embedding.mongodb.MongoDbEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

@Configuration
public class TeamAgentConfig {

    // llm langchain embedding용이긴 한데, 이 역시 혹시 몰라서 남겨둡니다.

    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    public MongoDbEmbeddingStore embeddingStore(EmbeddingModel embeddingModel,
                                         MongoClient mongoClient) {
        Boolean createIndex = true;
        IndexMapping indexMapping = IndexMapping.builder()
                .dimension(embeddingModel.dimension())
                .metadataFieldNames(new HashSet<>())
                .build();


        return MongoDbEmbeddingStore.builder()
                .databaseName("llm")
                .collectionName("student")
                .createIndex(createIndex)
                .indexName("vector_index")
                .indexMapping(indexMapping)
                .fromClient(mongoClient)
                .build();
    }

    EmbeddingStoreContentRetriever embeddingStoreContentRetrieve(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel
    ){
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(100)
                .minScore(0.85)
                .build();
    }



}
