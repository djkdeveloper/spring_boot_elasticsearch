package com.djk.es.store;

import lombok.Data;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.elasticsearch.core.ResultsMapper;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by dujinkai on 2018/6/12.
 * 店铺搜索响应
 */
@Data
public class EsStoreResponse {

    /**
     * 总记录数
     */
    private int total;

    /**
     * 店铺信息
     */
    private List<EsStore> esStores;

    /**
     * 构造es返回数据
     *
     * @param searchResponse es搜索结果
     * @param resultsMapper  结果转化器
     * @return 返回es数据
     */
    public static EsStoreResponse buildEsSearchResponse(SearchResponse searchResponse, ResultsMapper resultsMapper) {
        EsStoreResponse esStoreResponse = new EsStoreResponse();
        esStoreResponse.total = (int) searchResponse.getHits().getTotalHits();
        esStoreResponse.esStores = resultsMapper.mapResults(searchResponse, EsStore.class, null).getContent();

        // 设置距离
        Stream.of(searchResponse.getHits().getHits()).forEach(searchHitFields ->
                esStoreResponse.esStores.stream().forEach(esStore -> {
                    if (searchHitFields.getId().equals(esStore.getId())) {
                        esStore.setDistance((Double) searchHitFields.getSortValues()[0]+"");
                    }
                }));

        return esStoreResponse;
    }

}
