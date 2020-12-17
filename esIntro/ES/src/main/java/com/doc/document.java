package com.doc;

import com.entity.Book;
import com.utils.ESClient;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class document{
    RestHighLevelClient client = ESClient.getClient();
    String index = "book";
    String type = "novel";
    ObjectMapper mapper = new ObjectMapper();
    @Test
    public void createDoc() throws IOException {

        //      准备json数据
        Book book = new Book(2, "时间的秩序", "卡洛·罗韦利" ,345000,"为什么我们记得过去，而非未来？时间“流逝”意味着什么？是我们存在于时间之内，还是时间存在于我们之中？卡洛·罗韦利用诗意的文字，邀请我们思考这一亘古难题——时间的本质。在我们的直觉里，时间是全宇宙统一的，稳定地从过去流向未来，可以用钟表度量。可罗韦利向我们揭示出一个奇怪 的宇宙，在这里，时间的特质一一坍塌，在最基本的层面上，时间消失了。他告诉我们，我们对时间流逝的感知，取决于我们的视角……");
        String json = mapper.writeValueAsString(book);

        System.out.println(json);
        //      准备一个request对象(手动指定id创建)
        IndexRequest indexRequest = new IndexRequest(index,type,book.getId().toString());
        indexRequest.source(json,XContentType.JSON);

        //      通过client对象执行添加操作
        RestHighLevelClient client = ESClient.getClient();
        IndexResponse resp = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(resp.getResult().toString());
    }
//  doc方式修改文档
    @Test
    public void updateDoc() throws IOException {
//      创建map,指定需要修改的内容
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", "12312323");
        String Id = "1";
//      创建一个request对象，封装数据
        UpdateRequest updateRequest = new UpdateRequest(index, type, Id);
        updateRequest.doc(map);
//      通过client对象执行
        RestHighLevelClient client = ESClient.getClient();
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(update.getResult().toString());
    }
    @Test
    public void deleteDoc(){
        DeleteRequest deleteRequest = new DeleteRequest(index,type,"1");

    }




}
