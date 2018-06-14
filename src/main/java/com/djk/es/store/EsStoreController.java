package com.djk.es.store;

import com.djk.es.util.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dujinkai on 2018/6/11.
 * 店铺请求控制器
 */
@RestController
@RequestMapping("/store")
public class EsStoreController {

    /**
     * 注入店铺服务接口
     */
    @Autowired
    private EsStoreService esStoreService;

    /**
     * 新增或者修改店铺信息
     *
     * @param esStores 店铺信息
     * @return 返回1
     */
    @PostMapping
    @SysLog
    public int addStore(@RequestBody List<EsStore> esStores) {
        esStoreService.addStore(esStores);
        return 1;
    }

    @PostMapping("/query")
    @SysLog
    public EsStoreResponse query(@RequestBody EsStoreRequest esStoreRequest) {
        return esStoreService.queryStore(esStoreRequest);
    }
}
