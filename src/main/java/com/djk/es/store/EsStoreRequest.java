package com.djk.es.store;

import lombok.Data;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;


/**
 * Created by dujinkai on 2018/6/12.
 * 查询店铺请求实体
 */
@Data
public class EsStoreRequest {


    /**
     * 索引名称
     */
    private String indexName = "es_store_index";

    /**
     * 类型
     */
    private String type = "esStore";

    /**
     * 经度
     */
    private String lat;

    /**
     * 纬度
     */
    private String lon;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 距离 默认10km
     */
    private String distance = "10";

    /**
     * 分页大小
     */
    private int pageSize = 10;

    /**
     * 起始页
     */
    private int pageNum = 0;


    /**
     * 构造NativeSearchQueryBuilder
     *
     * @return 返回NativeSearchQueryBuilder
     */
    public NativeSearchQueryBuilder buildNativeSearchQueryBuilder() {
        return new NativeSearchQueryBuilder().withIndices(this.indexName).withTypes(this.type);
    }


    /**
     * 构造分页数据
     *
     * @return 返回分页
     */
    public Pageable buildPageable() {
        return new PageRequest(this.pageNum, this.pageSize);
    }

    /**
     * 构造bool 复合查询
     *
     * @return 返回bool复合查询
     */
    public BoolQueryBuilder buildBoolQueryBuilder() {

        // bool 查询 不懂自行补脑 https://es.xiaoleilu.com
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 店铺名称搜索
        this.addStoreNameQuery(boolQueryBuilder);

        // 增加附近店铺的搜索
        this.addDistanceQuery(boolQueryBuilder);


        return boolQueryBuilder;
    }


    /**
     * 附近店铺的搜索
     *
     * @param boolQueryBuilder boolQueryBuilder bool 查询
     */
    private void addDistanceQuery(BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.filter(QueryBuilders.geoDistanceQuery("address").point(Double.parseDouble(this.lat), Double.parseDouble(this.lon)).distance(Double.parseDouble(this.distance), DistanceUnit.KILOMETERS));
    }

    /**
     * 店铺名称搜索
     *
     * @param boolQueryBuilder bool 查询
     */
    private void addStoreNameQuery(BoolQueryBuilder boolQueryBuilder) {
        if (StringUtils.isEmpty(this.storeName)) {
            return;
        }

        boolQueryBuilder.must(QueryBuilders.matchQuery("storeName", this.storeName));
    }

    /**
     * 设置排序
     *
     * @param nativeSearchQueryBuilder 查询主体
     */
    public void buildSort(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder.withSort(SortBuilders.geoDistanceSort("address", Double.parseDouble(this.lat), Double.parseDouble(this.lon)).unit(DistanceUnit.KILOMETERS).order(SortOrder.ASC));
    }
}
