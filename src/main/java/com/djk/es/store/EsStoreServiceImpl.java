package com.djk.es.store;

import com.djk.es.product.EsSearchRequest;
import com.djk.es.product.EsSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by dujinkai on 2018/6/11.
 * es服务接口实现
 */
@Slf4j
@Service
public class EsStoreServiceImpl implements EsStoreService, InitializingBean {

    /**
     * 注入es店铺接口
     */
    @Autowired
    private EsStoreMapper esStoreMapper;

    /**
     * 注入es模版
     */
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 结果转化器
     */
    private ResultsMapper resultsMapper;

    @Override
    public void addStore(List<EsStore> esStores) {
        log.debug("addStore and esStores:{}", esStores);

        if (CollectionUtils.isEmpty(esStores)) {
            log.error("do not need to addStore");
            return;
        }

        esStoreMapper.saveAll(esStores);
    }

    @Override
    public EsStoreResponse queryStore(EsStoreRequest esStoreRequest) {
        log.debug("queryStore and esStoreRequest:{}", esStoreRequest);

        if (Objects.isNull(esStoreRequest)) {
            return new EsStoreResponse();
        }

        // 建立dsl搜索语句
        SearchQuery searchQuery = buildSearchQuery(esStoreRequest);

        log.info("search from Elasticsearch and DSL :{}", searchQuery.getQuery().toString());
        return elasticsearchTemplate.query(searchQuery, searchResponse -> {
            log.info("result:" + searchResponse);
            return EsStoreResponse.buildEsSearchResponse(searchResponse, resultsMapper);
        });
    }

    /**
     * 构造查询语句
     *
     * @param esStoreRequest 查询请求
     * @return 返回查询语句
     */
    private SearchQuery buildSearchQuery(EsStoreRequest esStoreRequest) {
        //设置查询主体
        NativeSearchQueryBuilder nativeSearchQueryBuilder = esStoreRequest.buildNativeSearchQueryBuilder().withPageable(esStoreRequest.buildPageable()).withQuery(esStoreRequest.buildBoolQueryBuilder());

        // 距离从近到远排序
        addSort(nativeSearchQueryBuilder, esStoreRequest);

        return nativeSearchQueryBuilder.build();
    }

    /**
     * 添加排序
     *
     * @param nativeSearchQueryBuilder 查询主题
     * @param request                  查询参数
     */
    private void addSort(NativeSearchQueryBuilder nativeSearchQueryBuilder, EsStoreRequest request) {
        request.buildSort(nativeSearchQueryBuilder);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        resultsMapper = new DefaultResultMapper(elasticsearchTemplate.getElasticsearchConverter().getMappingContext());
    }

}
