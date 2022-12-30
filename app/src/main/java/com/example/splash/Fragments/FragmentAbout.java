package com.example.splash.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.splash.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import org.jetbrains.annotations.NotNull;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentAbout extends BottomSheetDialogFragment {

    //FireStore references..
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = db.collection("Edit User Details").document("o4sg0A1Gc8WH1ZyRYTYZDjx0hMJ3");
    public ListenerRegistration listenerRegistration;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CircleImageView imageView = view.findViewById(R.id.dev1Img);

        listenerRegistration = documentReference.addSnapshotListener((value, error) -> {
            if (value.exists()) {
                String imageUrl = value.getString("imageUrl");
                Glide.with(this).load(imageUrl).into(imageView); //set profile Image

            }
        });
    }

}
