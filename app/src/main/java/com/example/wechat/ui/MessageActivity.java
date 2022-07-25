package com.example.wechat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.example.wechat.model.Users;
import com.example.wechat.model.messages;
import com.example.wechat.pojo.Constants;
import com.example.wechat.pojo.adapter.MessageAdapter;
import com.example.wechat.pojo.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView imageView,status;
    TextView username;
    Intent intent;
    EditText editText;
    ImageButton sendBtn;
    String receiverID;
    RecyclerView recyclerView;
    List<messages> messageData;
    MessageAdapter messageAdapter;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intent = getIntent();
        receiverID = intent.getStringExtra("userID");
        username = findViewById(R.id.userName);
        imageView = findViewById(R.id.circularImage);
        status = findViewById(R.id.statusImage);
        sendBtn = findViewById(R.id.sendBtn);
        editText = findViewById(R.id.editText);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().isEmpty()) {
                    String msg = editText.getText().toString();
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
                        Log.d("sender",FirebaseAuth.getInstance().getCurrentUser().getUid() );
                        Log.d("receiver",receiverID );

                        sendMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(), msg, receiverID);
                    }
                }else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }if (editText.length() > 0) {
                    editText.setText(null);
                }
            }
        });/*
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    updateTypingStatus("false");
                } else {
                    updateTypingStatus("true");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        recyclerView = findViewById(R.id.messageRV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        getReceiverData();
        displayMessageStatus(receiverID);
    }

    void getReceiverData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(receiverID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                assert user != null;
                username.setText(user.getUsername());
                if (user.getImageURL().equals("def")) {
                    imageView.setImageResource(R.drawable.image);
                } else {
                    Glide.with(MessageActivity.this.getApplicationContext()).load(user.getImageURL()).into(imageView);
                }
                if (user.getStatus().equals("online")) {
                    status.setVisibility(View.VISIBLE);
                } else {
                    status.setVisibility(View.GONE);
                }
                displayMessages(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()), receiverID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void sendMessage(String sender, String message, String receiver) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("message", message);
        hashMap.put("receiver", receiver);
        hashMap.put("messageStatus", false);

        reference.child("ChatData").push().setValue(hashMap);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").
                child(sender).child(receiver);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    databaseReference.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void displayMessages(String myID, String userID) {
        messageData = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messages messages = dataSnapshot.getValue(messages.class);
                    assert messages != null;
                    if ((messages.getReceiver().equals(myID) && messages.getSender().equals(userID))||
                            (messages.getSender().equals(myID) && messages.getReceiver().equals(userID))) {
                        messageData.add(messages);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, messageData);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    void displayMessageStatus(String receiverID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatData");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messages messages = dataSnapshot.getValue(messages.class);
                    assert messages != null;
                    Log.d("lastReciver", messages.getReceiver());
                    if (messages.getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && messages.getSender().equals(receiverID)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("messageStatus", true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
/*
    void updateTypingStatus(String status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(receiverID);
        Map<String, Object> map = new HashMap<>();
        map.put("typing", status);
        databaseReference.updateChildren(map);
    }
*/

    @Override
    protected void onResume() {
        super.onResume();
        new Constants().displayUserStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
        new Constants().displayUserStatus("offline");


    }
}