package com.example.splash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.splash.Model.ChatData;
import com.example.splash.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends ArrayAdapter<ChatData> {

    public ChatAdapter(Context context, int resource, List<ChatData> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.chat_sample_list, parent, false);
        }

        TextView userName = convertView.findViewById(R.id.chatUserName);
        TextView userMsg = convertView.findViewById(R.id.chatUserMessage);
        ImageView photoImageView = convertView.findViewById(R.id.chatUserImg);
        CircleImageView chatProfileImg = convertView.findViewById(R.id.chatProfileImg);

        ChatData message = getItem(position);

        boolean isPhoto = message.getPhotoUrl() != null; //Photo is Available
        if (isPhoto) {
            userMsg.setVisibility(View.GONE); //Hide the user message
            Glide.with(photoImageView.getContext()) //Show the image
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            userMsg.setVisibility(View.VISIBLE); //Show the user message
            userMsg.setText(message.getText());
            photoImageView.setVisibility(View.GONE); //Hide the image
        }
        userName.setText(message.getName());
        //Set user image..
        Glide.with(chatProfileImg.getContext())
                .load(message.getChatProfileUrl())
                .into(chatProfileImg);

        return convertView;
    }
}
