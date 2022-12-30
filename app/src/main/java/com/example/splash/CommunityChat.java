package com.example.splash;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splash.Adapter.ChatAdapter;
import com.example.splash.Model.ChatData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class CommunityChat extends AppCompatActivity {
    private static final int RC_PHOTO_PICKER = 1; //After photo chosen, it will use to identify as result
    EmojiconEditText chatMsg;
    EmojIconActions emojIconActions;
    TextView emptyTxt;
    ImageButton emoteBtn, pickImgBtn;
    ImageView sendBtn;
    Toolbar toolbar;
    ProgressBar progressBar;
    ListView listView;
    ChatAdapter chatAdapter;
    View view;

    //Firebase Init..
    //Firebase Realtime Database init
    public FirebaseDatabase mFirebaseDatabase;  //initializing Realtime database..
    public DatabaseReference mDatabaseReferences; //use to create child in Realtime databases.
    //Firebase storage init
    private FirebaseStorage mFirebaseStorage;   //For Firebase storage
    private StorageReference mChatPhotosStorageReference; //Creating child in Firebase storage.
    private ChildEventListener mChildEventListener; //for reading data from realtime database
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String mUsername = firebaseUser.getEmail();

    //FireStore references..
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId = firebaseUser.getUid();
    DocumentReference documentReference = db.collection("Edit User Details").document(userId);
    public ListenerRegistration listenerRegistration;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chat);

        //Assigning Ids..
        chatMsg = findViewById(R.id.chat_message);
        emoteBtn = findViewById(R.id.emoji);
        pickImgBtn = findViewById(R.id.pickImage);
        sendBtn = findViewById(R.id.sendBtn);
        progressBar = findViewById(R.id.chatProgress);
        listView = findViewById(R.id.list);
        emptyTxt = findViewById(R.id.emptyTxt);
        view = findViewById(R.id.root_view);

        //Firebase instances..
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReferences = mFirebaseDatabase.getReference().child("user chat");
        mFirebaseStorage = FirebaseStorage.getInstance(); //Getting into Firebase Storage
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("user chat photo"); //Creating child in storage..
        //Emoji stufss..
        emojIconActions = new EmojIconActions(this, view, chatMsg, emoteBtn);
        emojIconActions.ShowEmojIcon();

        //Toolbar
        toolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Change statusBar text color..
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // ImagePickerButton shows an image picker to upload a image for a message
        pickImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
        });

        // Enable Send button when there's text to send
        chatMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendBtn.setEnabled(true);
                    sendBtn.setColorFilter(getResources().getColor(R.color.green));
                    sendBtn.setAlpha(1.0f);
                } else {
                    sendBtn.setColorFilter(getResources().getColor(R.color.grey));
                    sendBtn.setEnabled(false);
                    sendBtn.setAlpha(0.6f);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Initialize message ListView and its adapter
        List<ChatData> message = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, R.layout.chat_sample_list, message);
        listView.setAdapter(chatAdapter);
        attachDatabaseReadListener();
        listView.setEmptyView(emptyTxt);

        //Handling Send Button Functionality..
        sendBtn.setOnClickListener(view -> {
            String userName = mUsername;
            //Apply listener for fetching user profile image from FireStore..
            listenerRegistration = documentReference.addSnapshotListener((documentSnapshot, error) -> {
                if (documentSnapshot.exists()) {
                    //Exist..
                    String imageUrl = documentSnapshot.getString("imageUrl");
                    ChatData friendlyMessage = new ChatData(chatMsg.getText().toString(), userName, null, imageUrl);
                    mDatabaseReferences.push().setValue(friendlyMessage);
                    chatMsg.setText(""); //Clear chat box..
                }
            });
        });
    }

    //Upload photo to firebase storage.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK && data != null) { //Getting image
                Uri selectedImageUri = data.getData();
                // Get a reference to store file at chat_photos/<FILENAME>
                final StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

                photoRef.putFile(selectedImageUri)
                        .addOnSuccessListener(this, taskSnapshot -> {
                            //When the image has successfully uploaded, get its download URL
                            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                Uri downloadUri = uri;
                                //Fetching profile image of user, when user share image in chat...
                                listenerRegistration = documentReference.addSnapshotListener(this, (documentSnapshot, error) -> {
                                    if (documentSnapshot.exists()) {
                                        //Exist..
                                        String imageUrl = documentSnapshot.getString("imageUrl");
                                        ChatData friendlyMessage = new ChatData(null, mUsername, downloadUri.toString(), imageUrl);
                                        mDatabaseReferences.push().setValue(friendlyMessage);
                                        chatMsg.setText("");
                                    }
                                });

                            });
                        });

            }
        } catch (Exception e) {
            Toast.makeText(this, "Photo sent failed", Toast.LENGTH_SHORT).show();
        }
    }

    //Reading Data from realtime databases..
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatData message = dataSnapshot.getValue(ChatData.class);
                    chatAdapter.add(message);
                    listView.setSelection(chatAdapter.getCount() - 1); //Always display last messages that added.
                    progressBar.setVisibility(View.GONE);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabaseReferences.addChildEventListener(mChildEventListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.colorBlack: {
                listView.setBackgroundColor(Color.BLACK);
                break;
            }
            case R.id.colorBlue: {
                listView.setBackgroundColor(Color.BLUE);
                break;
            }
            case R.id.darkGrey: {
                listView.setBackgroundColor(Color.DKGRAY);
                break;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}