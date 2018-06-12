# spring_boot_elasticsearch

springboot和elasticsearch的结合
用户需要自己去下载安装elasticsearch 并且elasticsearch 的版本必须是5.5.0的
因为分词用到了ik分词器 所以用户还要给elasticsearch安装ik插件，ik的插件也是5.5.0的
elasticsearch 和ik的版本必须都是5.5.0 因为我们的应用是以一个节点的身份加入es集群的

该项目主要完成的是
1：将产品数据加入es和更新
2：删除es中的产品
3：搜索产品 （根据产品名称，过滤，排序，属性搜索，搜索聚合）
4：将店铺数据加入es和更新
5：搜索附近的店铺从近到远排序


1:新增或者更新es中的产品数据(这边使用的是postman插件)

http://localhost:8080/es  POST

[{"id":"5","pname":"联想电脑下架","stock":12,"price":"8000","status":"0","saleNum":"100","fcateId":1,"scateId":2,"tcateId":3,"esAttributes":[{"attributeName":"内存","attributeValue":"32g"},{"attributeName":"颜色","attributeValue":"金色"}]}]


2:删除es中的产品数据
http://localhost:8080/es?ids=3&ids=4 DELETE

3:搜索es中的产品数据
http://localhost:8080/es/query POST
{"stockFilter":"0","sortItems":[{"field":"price","order":"1"}],"attributes":[{"name":"版本","values":["版本1"]}]}

4:将店铺数据加入es和更新
http://localhost:8080/store POST
[{"id":"4","storeName":"华为南京研究所 约34米","address":"31.9790312860,118.7695026398"}]

5:搜索附近的店铺从近到远排序
http://localhost:8080/store/query POST
{"lat":"31.9889322580","lon":"118.7710475922","storeName":"店铺"}