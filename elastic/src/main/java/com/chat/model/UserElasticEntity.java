package com.chat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;

@Data
@Builder
@Document(indexName = "users", createIndex = true)
@Setting(sortFields = {"name", "username"}, sortModes = {Setting.SortMode.max, Setting.SortMode.min}, sortOrders = {Setting.SortOrder.desc, Setting.SortOrder.asc}, sortMissingValues = {Setting.SortMissing._last, Setting.SortMissing._first}, shards = 1, replicas = 1, refreshInterval = "1s", indexStoreType = "fs")
public class UserElasticEntity implements ElasticIndexModel<Long>, Persistable<Long> {
    @Id
    @JsonProperty
    private Long id;

    @Field(type = FieldType.Keyword)
    @JsonProperty
    private String username;

    @Field(type = FieldType.Keyword)
    @JsonProperty
    private String name;

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ssZZ")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ssZZ")
    @JsonProperty
    private Date createdAt;

    @Override
    public boolean isNew() {
        return id == null || createdAt == null;
    }
}
