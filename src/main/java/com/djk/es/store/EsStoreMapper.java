package com.djk.es.store;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by dujinkai on 2018/6/11.
 * es店铺数据库接口
 */
public interface EsStoreMapper extends ElasticsearchRepository<EsStore, String> {
}
