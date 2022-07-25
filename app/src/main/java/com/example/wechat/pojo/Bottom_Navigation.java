package com.example.wechat.pojo;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.wechat.R;
import com.example.wechat.ui.fragment.All_Users;
import com.example.wechat.ui.fragment.Chat;
import com.example.wechat.ui.fragment.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bottom_Navigation extends AppCompatActivity {
    public static final int PERMISSIONS_CODE = 10;
    String[] permissions = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
    };
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        loadFragment(new Chat());
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_chat )
            {
                loadFragment(new Chat());
            }
            else if (item.getItemId() == R.id.navigation_all_users) {
                loadFragment(new All_Users());
            }
            else if (item.getItemId() == R.id.navigation_settings) {
                loadFragment(new Settings());
            }
            return true;
        });
        checkAndRequestPermission();
    }
    private void loadFragment(Fragment fragment) {
        //replace the fragment
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout, fragment, null)
                .commit();
    }
    void checkAndRequestPermission() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), PERMISSIONS_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permissions granted.
                Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
            } else {
                // no permissions granted.
                Toast.makeText(this, "no permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        new Constants().displayUserStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Constants().displayUserStatus("offline");

    }
}