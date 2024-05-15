package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

class ChatMessage {
    private int type;
    private String message;

    public ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMessage> messages;
    private Context context;

    public ChatAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView avatarImageView;
        TextView messageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        public void bind(ChatMessage message) {

            avatarImageView.setImageResource(message.getType() == 0 ? R.drawable.bot : R.drawable.user);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) avatarImageView.getLayoutParams();
            if (message.getType() == 0) {
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.removeRule(RelativeLayout.ALIGN_PARENT_END);
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.removeRule(RelativeLayout.ALIGN_PARENT_START);
            }

            messageTextView.setText(message.getMessage());
            params = (RelativeLayout.LayoutParams) messageTextView.getLayoutParams();
            if (message.getType() == 0) {
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.removeRule(RelativeLayout.ALIGN_PARENT_END);
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.removeRule(RelativeLayout.ALIGN_PARENT_START);
            }
            messageTextView.setLayoutParams(params);
        }
    }
}
