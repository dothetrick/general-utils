package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

public class HttpUtil {
    private CloseableHttpClient client = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectTimeout(500) //连接超时时间，单位毫秒，按实际情况调整
                    .build())
            .setDefaultSocketConfig(SocketConfig.custom()
                    .setSoTimeout(2 * 1000) //请求响应超时时间，单位毫秒，这里设为2秒，按实际情况调整
                    .build())
            .build();

    public CloseableHttpClient getClient() {
        return client;
    }

    public void setClient(CloseableHttpClient client) {
        this.client = client;
    }

    /**
     * Get请求
     * @param url
     * @return
     * @throws IOException
     */
    public String get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse httpResponse = client.execute(httpGet)) {
            HttpEntity httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity, "UTF-8");
        }
    }

    /**
     * POST请求
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public String post(String url, Map<String, String> params) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse httpResponse = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(params);
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            httpPost.setEntity(stringEntity);
            httpPost.addHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
            httpResponse = client.execute(httpPost);
            return EntityUtils.toString(httpResponse.getEntity());
        } finally {
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
    }
}
