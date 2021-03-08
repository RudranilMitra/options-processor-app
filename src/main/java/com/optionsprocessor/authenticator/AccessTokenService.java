package com.optionsprocessor.authenticator;

import lombok.SneakyThrows;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class AccessTokenService {
    @Value("${app.refresh_token}")
    private String refreshToken;
    @Value("${app.code}")
    private String code;
    @Value("${app.client_id}")
    private String clientID;
    @Value("${app.url}")
    private String url;
    @Value("${app.redirect_uri}")
    private String redirectUri;

    @SneakyThrows
    public String generateAccessToken() {
        Map values = new HashMap<String, String>() {
            {
                put("grant_type", "refresh_token");
                put("refresh_token", refreshToken);
                put("code", code);
                put("client_id", clientID);
                put("redirect_uri", redirectUri);
            }
        };

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(values))
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONParser p = new JSONParser(response.body());
        Map m = (LinkedHashMap) p.parse();

        return (String) m.get("access_token");

    }

    private HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
