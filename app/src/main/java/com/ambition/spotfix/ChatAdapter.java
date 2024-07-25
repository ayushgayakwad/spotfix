package com.ambition.spotfix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> messages;

    public ChatAdapter(Context context, ArrayList<String> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String message = getItem(position);

        if (convertView == null) {
            if (message.startsWith("You: ")) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chat_bubble_user, parent, false);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.chat_bubble_bot, parent, false);
            }
        }

        TextView chatMessage = convertView.findViewById(R.id.chatMessage);
        chatMessage.setText(message);

        return convertView;
    }
}

