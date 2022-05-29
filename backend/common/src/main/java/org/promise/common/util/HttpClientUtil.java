package org.promise.common.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author moqi
 * @date 2022/5/16 20:09
 */
public class HttpClientUtil {


    public static String doGet(String url, Map<String, Object> param) throws Exception {
        if (url == null) {
            return null;
        }
        CloseableHttpClient client = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder(url);
        if (param != null) {
            for (Map.Entry<String, Object> stringObjectEntry : param.entrySet()) {
                uriBuilder.addParameter(stringObjectEntry.getKey(), stringObjectEntry.getValue().toString());
            }
        }
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        CloseableHttpResponse response = client.execute(httpGet);
        String res = EntityUtils.toString(response.getEntity(), "utf-8");
        client.close();
        return res;
    }

}
