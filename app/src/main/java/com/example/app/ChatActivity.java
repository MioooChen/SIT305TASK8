package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {
    String username;
    List<ChatMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        messages = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.chat_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        ChatAdapter chatAdapter = new ChatAdapter(this, messages);
        recyclerView.setAdapter(chatAdapter);

        EditText textMessage = findViewById(R.id.message_edittext);


        ImageButton btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(v -> {
            if (textMessage.getText().length() == 0) {
                textMessage.setError("Message cannot be empty");
                return;
            }
            String message = textMessage.getText().toString();
            textMessage.setText("");
            messages.add(new ChatMessage(1, message));
            chatAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messages.size());
            NetUtil.sendMessage(createPayload(message), new NetUtil.NetCallback() {
                @Override
                public void onSuccess(String response) {
                    messages.add(new ChatMessage(0, response));
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messages.size());
                }

                @Override
                public void onError(String errorMessage) {
                    System.out.println(errorMessage);
                    Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    String createPayload(String message) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("userMessage", message);
        List<Map<String, String>> chatHistory = new ArrayList<>();
        for (int i = 0; i < messages.size() / 2 * 2; i+=2) {
            Map<String, String> chatMessage = new HashMap<>();
            chatMessage.put("User", messages.get(i).getMessage());
            chatMessage.put("Llama", messages.get(i+1).getMessage());
            chatHistory.add(chatMessage);
        }
        jsonObject.put("chatHistory", chatHistory);

        Gson gson = new Gson();
        return gson.toJson(jsonObject);
    }
}