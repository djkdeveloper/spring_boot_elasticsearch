package com.djk.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by dujinkai on 2018/6/11.
 * 产品属性实体
 */
@Data
public class EsAttribute {

    /**
     * 属性名称
     */
    @Field(type = FieldType.Keyword)
    private String attributeName;

    /**
     * 属性值
     */
    @Field(type = FieldType.Keyword)
    private String attributeValue;
}
