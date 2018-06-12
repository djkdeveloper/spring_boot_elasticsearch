package com.djk.es.store;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

/**
 * Created by dujinkai on 2018/6/11.
 * 店铺信息
 */
@Data
@Document(indexName = "es_store_index", type = "esStore")
public class EsStore {

    /***
     * 主键id
     */
    @Id
    @Field(type = FieldType.Keyword, store = true)
    private String id;

    /**
     * 店铺名称
     */
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_smart", type = FieldType.Text, store = true)
    private String storeName;

    /**
     * 地址
     */
    @GeoPointField
    private String address;

    /**
     * 距离
     */
    private String distance;
}
