package com.djk.es;

import java.util.List;

/**
 * Created by dujinkai on 2018/6/11.
 * es服务接口
 */
public interface EsService {

    /**
     * 将产品加入es中
     *
     * @param esProducts 产品
     */
    void addProductToEs(List<EsProduct> esProducts);

    /**
     * 根据产品id删除产品索引
     *
     * @param ids 产品id
     */
    void deleteProducts(String... ids);

    /**
     * 搜索产品
     *
     * @param esSearchRequest 搜索请求
     * @return 返回产品信息
     */
    EsSearchResponse query(EsSearchRequest esSearchRequest);
}
