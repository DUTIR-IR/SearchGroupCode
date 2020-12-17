package com.index;

import com.utils.ESClient;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class createIndex {
       //创建RestHighLevelClient
        RestHighLevelClient client = ESClient.getClient();
        String index = "book";
        String type = "novel";
        @Test
        public void createIndex() throws IOException {
            //准备关于索引的settings
            Settings.Builder settings = Settings.builder()
                    .put("number_of_shards", 3)
                    .put("number_of_replicas", 1);


            //准备关于索引的结构mappings es提供
            XContentBuilder mappings = JsonXContent.contentBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("name")
                    .field("type","text")
                    .field("analyzer", "ik_max_word")
                    .endObject()
                    .startObject("author")
                    .field("type","keyword")
                    .endObject()
                    .startObject("count")
                    .field("type","long")
                    .endObject()
                    .startObject("desc")
                    .field("type","text")
                    .field("analyzer", "ik_max_word")
                    .endObject()
                    .endObject()
                    .endObject();

            //将settings 和 mappings封装成一个request对象
            CreateIndexRequest request = new CreateIndexRequest(index)
                    .settings(settings)
                    .mapping(type,mappings);
            //通过client对象去链接es并执行创建索引
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

            //测试
            System.out.println("response"+response.toString());

        }


}
