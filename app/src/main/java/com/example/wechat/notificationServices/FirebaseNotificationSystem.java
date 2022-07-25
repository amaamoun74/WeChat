package com.example.wechat.notificationServices;

import androidx.annotation.NonNull;

import com.example.wechat.model.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

public class FirebaseNotificationSystem extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        updateToken(s);
        super.onNewToken(s);
    }

    private void updateToken(String token) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
            Token token1 = new Token(token);
            databaseReference.child(firebaseUser.getUid()).setValue(token1);
        }
    }
}
