package com.djk.es.store;

import java.util.List;

/**
 * Created by dujinkai on 2018/6/11.
 * es服务接口
 */
public interface EsStoreService {

    /**
     * 添加店铺到索引
     *
     * @param esStores 店铺信息
     */
    void addStore(List<EsStore> esStores);

    /**
     * 查询附近的店铺
     *
     * @param esStoreRequest 请求信息
     * @return 返回附近的店铺
     */
    EsStoreResponse queryStore(EsStoreRequest esStoreRequest);
}
