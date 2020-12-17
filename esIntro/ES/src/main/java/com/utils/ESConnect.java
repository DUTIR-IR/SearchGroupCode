package com.utils;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;

public class ESConnect {
    @Test
    public void ESConnect() {
        RestHighLevelClient client = ESClient.getClient();
        System.out.println("已连接");
    }
}