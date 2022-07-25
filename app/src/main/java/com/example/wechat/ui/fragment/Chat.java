package com.example.wechat.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.example.wechat.model.ChatList;
import com.example.wechat.model.Token;
import com.example.wechat.model.Users;
import com.example.wechat.model.messages;
import com.example.wechat.pojo.Bottom_Navigation;
import com.example.wechat.pojo.adapter.UserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends Fragment {

    CircleImageView imageView;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Users> userData;
    UserAdapter userAdapter;
    ArrayList<ChatList> userArrayList;
    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        imageView = view.findViewById(R.id.circularImage);
        username = view.findViewById(R.id.userName);
        recyclerView = view.findViewById(R.id.userRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userArrayList = new ArrayList<>();
        userData = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getMyData();
        getChatList();
        getToken();
        return view;
    }

    void getMyData() {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                if (user != null) {
                    username.setText(user.getUsername());
                    if (user.getImageURL().equals("def")) {
                        imageView.setImageResource(R.drawable.image);
                    } else {
                        Glide.with(requireContext()).load(user.getImageURL()).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void getChatList() {
        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatList chatList = dataSnapshot.getValue(ChatList.class);
                    userArrayList.add(chatList);
                }
                displayChatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void displayChatList() {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);

                    for (ChatList chatList : userArrayList) {
                        if (users.getId().equals(chatList.getId())) {
                            userData.add(users);

                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), userData, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateToken(String token) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
            Token token1 = new Token(token);
            databaseReference.child(firebaseUser.getUid()).setValue(token1);
        }
    }

    void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                }
                // Get new FCM registration token
                token = task.getResult();
                updateToken(token);
            }
        });
    }
}
