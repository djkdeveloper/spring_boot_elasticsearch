package com.djk.es.product;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by dujinkai on 2018/6/11.
 * es接口
 */
public interface EsMapper extends ElasticsearchRepository<EsProduct, String> {
}
