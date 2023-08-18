package cn.hanglok.pacs.util;

import jakarta.annotation.Nullable;
import okhttp3.*;

import java.io.*;
import java.util.Map;

/**
 *  @className OkHttpUtils
 *  @description http请求工具类，基于OkHttp实现
 *  @author Allen
 *  @date 2023/5/22 10:18
 *  @version 1.0
 */
public class OkHttpUtils {
    public static void get(String url, @Nullable Map<String, String> header) {
        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder().url(url);
        if (header != null) {
            header.forEach(builder::addHeader);
        }
        Request request =builder.build();

        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            System.out.println("ResponseCode: " + responseCode);

            ResponseBody responseBody = response.body();
            String responseBodyString = responseBody.string();

            System.out.println("Response Body: " + responseBodyString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void post(String url, String requestBody, String mediaTypeString) {
        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        MediaType mediaType = MediaType.parse(mediaTypeString);
        RequestBody body = RequestBody.create(requestBody, mediaType);

        // 构建POST请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            System.out.println("Response Code: " + responseCode);

            ResponseBody responseBody = response.body();
            String responseBodyString = responseBody.string();

            System.out.println("Response Body: " + responseBodyString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void post(String url, String requestBody) {
        post(url, requestBody, "application/json");
    }

    public static void uploadFile(String uploadUrl, File file) {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            int statusCode = response.code();
            if (statusCode == 200) {
                // 上传成功，处理服务器响应
                // ...
            } else {
                // 上传失败
                // ...
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
