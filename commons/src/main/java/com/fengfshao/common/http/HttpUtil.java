package com.fengfshao.common.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单的http访问，适用于json协议的http接口，默认长连接 <br/>
 * <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html">关于JDK的HttpURLConnection长连接文档</a>
 *
 * @author fengfshao
 */
@Slf4j
public class HttpUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> post(String urlStr, Map<String, Object> body) {
        return post(urlStr, Collections.emptyMap(), body);
    }

    /**
     * 发起post请求，将接口返回的json反序列化为map
     * @param urlStr url
     * @param headers 请求头参数
     * @param body 请求体
     * @return 接口响应
     */
    public static Map<String, Object> post(String urlStr, Map<String, String> headers, Map<String, Object> body) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            headers.forEach(con::setRequestProperty);
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);

            String jsonBody = mapper.writeValueAsString(body);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            return mapper.readValue(content.toString(),
                    new TypeReference<Map<String, Object>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
}
