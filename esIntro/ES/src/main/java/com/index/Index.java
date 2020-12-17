package com.index;

import com.utils.ESClient;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Index {
    RestHighLevelClient client = ESClient.getClient();
    String index = "book";
    String type = "novel";
    //索引是否存在
    @Test
    public void exists() throws IOException {
        //1 准备request对象
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        // 2 通过client去检查
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    //删除索引
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest();
        request.indices(index);
        AcknowledgedResponse ack = client.indices().delete(request,RequestOptions.DEFAULT);
        System.out.println(ack);
    }
}

