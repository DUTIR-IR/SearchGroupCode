package com.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ESClient {
    public static RestHighLevelClient getClient(){
        //创建HttpHost对象
        HttpHost host = new HttpHost("127.0.0.1",9200);
        //创建RestClientBuilder
        RestClientBuilder clientBuilder = RestClient.builder(host);
        //创建RestHighLevelClient
        RestHighLevelClient client = new RestHighLevelClient(clientBuilder);

        return  client;
    }

}
