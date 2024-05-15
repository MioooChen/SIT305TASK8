package com.example.app;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

import com.google.gson.annotations.SerializedName;

class MessageResponse {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}

public class NetUtil {
    private static final String BASE_URL = "http://10.0.2.2:5000/";

    // 定义 Retrofit 接口
    interface ChatService {
        @POST("chat")
        Call<MessageResponse> sendMessage(@Body RequestBody requestBody);
    }

    public static void sendMessage(String jsonPayload, final NetCallback callback) {
        // 创建 Retrofit 实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建 Retrofit 服务接口实例
        ChatService service = retrofit.create(ChatService.class);

        // 创建请求体
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonPayload);

        // 发送 POST 请求
        Call<MessageResponse> call = service.sendMessage(requestBody);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();
                    if (messageResponse != null) {
                        String message = messageResponse.getMessage();
                        // 在这里处理返回的消息
                        callback.onSuccess(message);
                    } else {
                        callback.onError("Empty response body");
                    }
                } else {
                    callback.onError("Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // 回调接口定义
    public interface NetCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
}
