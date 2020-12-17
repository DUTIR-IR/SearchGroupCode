package com.doc;

import com.utils.ESClient;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.BoostingQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class query {
    ObjectMapper mapper = new ObjectMapper();
    RestHighLevelClient client = ESClient.getClient();
    String index = "book";
    String type = "novel";

    @Test
    //term查询
    public void termQuery() throws IOException {
        // 创建Request对象
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        // 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from(0);
        builder.size(5);
        builder.query(QueryBuilders.termQuery("author", "塔拉·韦斯特弗"));
        request.source(builder);
       // 执行查询
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //获取到_source中的数据
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> result = hit.getSourceAsMap();
            System.out.println(result);
    }

    }

    @Test
    public void test_terms() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termsQuery("author","塔拉·韦斯特弗","特德·姜"));

        request.source(builder);

        RestHighLevelClient client = ESClient.getClient();
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : resp.getHits().getHits()){
            System.out.println(hit);
        }
    }
    @Test
    public void match_all() throws IOException {
        // 创建Request  ,放入索引和类型
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        //指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        request.source(builder);
        builder.size(20); //es默认查询结果只展示10条，这里可以指定展示的条数
        // 执行查询
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()){
            System.out.println(hit);
        }
    }
    @Test
    public void match_field() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("desc","生命"));
        request.source(builder);
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()){
            System.out.println(hit);
        }

    }
    @Test
    public void match_boolean() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("desc", "人造 生命").operator(Operator.AND));
        request.source(builder);
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit);
        }
    }
    @Test
    public void multi_match() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 查询的文本内容  字段1 字段2 字段3 。。。。。
        builder.query(QueryBuilders.multiMatchQuery("秒", "author", "name"));
        request.source(builder);
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit);
        }
    }
    //id查询
    @Test
    public void searchbyId() throws IOException {
        GetRequest request = new GetRequest(index,type,"1");
        RestHighLevelClient client = ESClient.getClient();
        GetResponse resp = client.get(request, RequestOptions.DEFAULT);
        System.out.println(resp.getSourceAsMap());
    }
    //ids查询
    @Test
    public void test_query_ids() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.idsQuery().addIds("1","2","3"));
        request.source(builder);
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()){
            System.out.println(hit.getSourceAsMap());
        }

    }
    //perfix查询
    @Test
    public void query_prefix() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.prefixQuery("author","特"));
        request.source(builder);
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()){
            System.out.println(hit.getSourceAsMap());
        }
    }
    //fuzzy查询
    @Test
    public void query_fuzzy() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.fuzzyQuery("author","特德·浆").prefixLength(2));
        request.source(builder);
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()){
            System.out.println(hit.getSourceAsMap());
        }
    }
    //wildcard
    @Test
    public void query_wildcard() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.wildcardQuery("name","*吸"));
        request.source(builder);
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()){
            System.out.println(hit.getSourceAsMap());
        }
    }
    // 范围查询
    @Test
    public void query_range() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.rangeQuery("count").lt(1000).gt(30000));
        request.source(builder);
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()){
            System.out.println(hit.getSourceAsMap());
        }
    }

    //boolSearch
    @Test
    public void  boolSearch() throws IOException {

        //  1.创建 searchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 或
        boolQueryBuilder.should(QueryBuilders.termQuery("author","顾湘"));
        boolQueryBuilder.should(QueryBuilders.termQuery("author","罗新"));

        //not
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("Id",2));

        //desc 包含 人 和 我
        boolQueryBuilder.must(QueryBuilders.matchQuery("desc","人"));
        boolQueryBuilder.must(QueryBuilders.matchQuery("desc","我"));

        builder.query(boolQueryBuilder);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }
    @Test
    public void  boostSearch() throws IOException {

        SearchRequest request = new SearchRequest(index);
        request.types(type);
        //指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoostingQueryBuilder boost = QueryBuilders.boostingQuery(
                QueryBuilders.matchQuery("desc", "天"),  //，
                QueryBuilders.matchQuery("desc", "生命")
        ).negativeBoost(0.5f);
        builder.query(boost);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }
    @Test
    public void highLightQuery() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        // 指定查询条件，指定高亮
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("desc","天空"));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("desc",10)
                .preTags("<font colr='red'>")
                .postTags("</font>");
        builder.highlighter(highlightBuilder);
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getHighlightFields().get("desc"));
        }
    }
    @Test
    public void query_scroll() throws IOException {
        // 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        //  指定scroll信息,生存时间
        request.scroll(TimeValue.timeValueMinutes(1L));
        // 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(2);
        builder.sort("count", SortOrder.DESC);
        builder.query(QueryBuilders.matchAllQuery());
        //获取返回结果scrollid ,source
        request.source(builder);
        RestHighLevelClient client = ESClient.getClient();
        SearchResponse response = client.search(request,RequestOptions.DEFAULT);
        String scrollId = response.getScrollId();
        System.out.println(scrollId);
        while(true){
            //  循环创建SearchScrollRequest
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            // 6 指定scrollid生存时间
            scrollRequest.scroll(TimeValue.timeValueMinutes(1L));
            // 执行查询获取返回结果
            SearchResponse scrollResp = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            // 判断是否得到数据，输出
            if (scrollResp.getHits().getHits() != null && scrollResp.getHits().getHits().length > 0){
                System.out.println("=======下一页的数据========");
                for (SearchHit hit : scrollResp.getHits().getHits()){
                    System.out.println(hit.getSourceAsMap());
                }
            }else{
                //        判断没有查询到数据-退出循环
                System.out.println("无");
                break;
            }
        }
        // 创建clearScrollRequest
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        // 指定scrollid
        clearScrollRequest.addScrollId(scrollId);
        // 删除
        client.clearScroll(clearScrollRequest,RequestOptions.DEFAULT);
    }
}
