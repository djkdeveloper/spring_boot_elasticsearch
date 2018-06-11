package com.djk.es;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dujinkai on 2018/6/11.
 * es服务接口
 */
@Service
@Slf4j
public class EsServiceimpl implements EsService, InitializingBean {

    /**
     * 注入es接口
     */
    @Autowired
    private EsMapper esMapper;

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
    public void addProductToEs(List<EsProduct> esProducts) {
        log.debug("addProductToEs and esProducts:{}", esProducts);

        if (CollectionUtils.isEmpty(esProducts)) {
            log.error("do not need to addProductToEs due to esProducts is empty....");
            return;
        }

        // 将产品加入es中
        esMapper.saveAll(esProducts);
    }

    @Override
    public void deleteProducts(String... ids) {
        log.debug("deleteProducts and ids:{}", ids);

        if (ArrayUtils.isEmpty(ids)) {
            log.error("do not need to deleteProducts....");
            return;
        }
        esMapper.deleteAll(Stream.of(ids).map(EsProduct::buildById).collect(Collectors.toList()));
    }

    @Override
    public EsSearchResponse query(EsSearchRequest esSearchRequest) {
        log.debug("query and esSearchRequest:{}", esSearchRequest);

        if (Objects.isNull(esSearchRequest)) {
            log.error("esSearchRequest is empty....");
            return new EsSearchResponse();
        }

        // 建立dsl搜索语句
        SearchQuery searchQuery = buildSearchQuery(esSearchRequest);

        log.info("search from Elasticsearch and DSL :{}", searchQuery.getQuery().toString());

        return elasticsearchTemplate.query(searchQuery, searchResponse -> {
            log.debug("result:" + searchResponse);
            return EsSearchResponse.buildEsSearchResponse(searchResponse, resultsMapper);
        });

    }

    /**
     * 构造查询语句
     *
     * @param esSearchRequest 查询请求
     * @return 返回查询语句
     */
    private SearchQuery buildSearchQuery(EsSearchRequest esSearchRequest) {
        //设置查询主体
        NativeSearchQueryBuilder nativeSearchQueryBuilder = esSearchRequest.buildNativeSearchQueryBuilder().withPageable(esSearchRequest.buildPageable()).withQuery(esSearchRequest.buildBoolQueryBuilder());

        // 设置排序
        addSort(nativeSearchQueryBuilder, esSearchRequest);

        // 聚合
        if (esSearchRequest.isNeddAgg()) {
            // 增加聚合
            addAttributeAggregation(nativeSearchQueryBuilder);
        }


        if (esSearchRequest.isHightLight()) {
            // 设置高亮
            setHighlighted(nativeSearchQueryBuilder);
        }

        return nativeSearchQueryBuilder.build();
    }

    /**
     * 设置高亮
     *
     * @param nativeSearchQueryBuilder 查询主体
     */
    private void setHighlighted(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("pname").preTags("<span class='red'>").postTags("</span>"));
    }


    /**
     * 添加排序
     *
     * @param nativeSearchQueryBuilder 查询主题
     * @param request                  查询参数
     */
    private void addSort(NativeSearchQueryBuilder nativeSearchQueryBuilder, EsSearchRequest request) {
        request.buildSort(nativeSearchQueryBuilder);
    }


    /**
     * 增加属性的聚合
     *
     * @param nativeSearchQueryBuilder 查询主体
     */
    private void addAttributeAggregation(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.nested("esAttributes", "esAttributes").subAggregation(AggregationBuilders.terms("attributeName").field("esAttributes.attributeName").subAggregation(AggregationBuilders.terms("attributeValue").field("esAttributes.attributeValue"))));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        resultsMapper = new DefaultResultMapper(elasticsearchTemplate.getElasticsearchConverter().getMappingContext());
    }
}
