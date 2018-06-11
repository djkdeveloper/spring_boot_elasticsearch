package com.djk.es;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by dujinkai on 2018/6/11.
 * es 产品实体
 */
@Data
@Document(indexName = "es_product_index", type = "esProduct")
public class EsProduct {

    /***
     * 主键id
     */
    @Id
    @Field(type = FieldType.Keyword, store = true)
    private String id;


    /**
     * 产品名称
     */
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_smart", type = FieldType.Text, store = true)
    private String pName;

    /**
     * 库存
     */
    @Field(type = FieldType.Integer)
    private int stock;


    /**
     * 价格
     */
    @Field(type = FieldType.Double)
    private BigDecimal price;

    /**
     * 上架状态 0 下架  1上架
     */
    @Field(type = FieldType.Keyword)
    private String status;

    /**
     * 销量
     */
    @Field(type = FieldType.Long)
    private long saleNum;

    /**
     * 上架 时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime upTime = LocalDateTime.now();

    /**
     * 一级分类id
     */
    @Field(type = FieldType.Long)
    private long fCateId;

    /**
     * 二级分类id
     */
    @Field(type = FieldType.Long)
    private long sCateId;

    /**
     * 三级分类id
     */
    @Field(type = FieldType.Long)
    private long tCateId;

    /**
     * 单品的属性值信息
     */
    @Field(type = FieldType.Nested)
    private List<EsAttribute> esAttributes;

    /**
     * 根据id构造es产品对象
     *
     * @param id 主键id
     * @return 返回es产品对象
     */
    public static EsProduct buildById(String id) {
        EsProduct esProduct = new EsProduct();
        esProduct.id = id;
        return esProduct;
    }
}
