package com.dreamflow.api.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "songs")
@Data
public class SongDocument {
    @Id
    private int songId;
    @Field(type=FieldType.Text,name = "song_name")
    private String songName;
    @Field(type=FieldType.Text, name = "tags")
    private String tags;
    @Field(type=FieldType.Text, name = "lyrics")
    private String lyrics;
    @Field(type=FieldType.Keyword, name = "genre")
    private String genre;
    @Field(type=FieldType.Dense_Vector, similarity = "cosine", dims=384)
    private float[] embedding;
}
