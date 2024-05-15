package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.edit_username);

        Button button = findViewById(R.id.btn_login);
        button.setOnClickListener(v -> {
            if (editText.getText().length() == 0) {
                editText.setError("Username cannot be empty");
                return;
            }

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("username", editText.getText());
            startActivity(intent);
        });

    }
}