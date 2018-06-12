package com.djk.es.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dujinkai on 2018/6/11.
 * es控制器
 */
@RestController
@RequestMapping("/es")
public class EsController {

    /**
     * 注入es服务接口
     */
    @Autowired
    private EsService esService;

    /**
     * 将产品加入es
     *
     * @param esProducts 产品信息
     * @return 返回1
     */
    @PostMapping
    public int addProduct(@RequestBody List<EsProduct> esProducts) {
        esService.addProductToEs(esProducts);
        return 1;
    }


    /**
     * 删除产品索引
     *
     * @param ids 产品id
     * @return 返回1
     */
    @DeleteMapping
    public int deleteProduct(String... ids) {
        esService.deleteProducts(ids);
        return 1;
    }


    /**
     * 搜索产品
     *
     * @param esSearchRequest 搜索请求
     * @return 返回搜索结果
     */
    @PostMapping("/query")
    public EsSearchResponse query(@RequestBody EsSearchRequest esSearchRequest) {
        return esService.query(esSearchRequest);
    }
}
