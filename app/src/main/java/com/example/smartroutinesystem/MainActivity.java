package com.example.smartroutinesystem;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView IdShow, welcome;
    Button rutine, home, test, showRoutine, showNotice;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdShow = findViewById(R.id.userIdCheck);
        welcome = findViewById(R.id.user_details);
        auth = FirebaseAuth.getInstance();
        rutine = findViewById(R.id.btn_rtn);
        home = findViewById(R.id.btn_home);
        test = findViewById(R.id.btn_test);
        showRoutine = findViewById(R.id.btn_show_routine);
        showNotice=findViewById(R.id.btn_show_notice);

        // Initially setting rutine button to GONE
        rutine.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String Uid = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("users");

        reference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("fullName").getValue(String.class);
                    String admin = snapshot.child("admin").getValue(String.class);
                    String cr = snapshot.child("cr").getValue(String.class);
                    String dept = snapshot.child("department").getValue(String.class);
                    String series = snapshot.child("series").getValue(String.class);
                    String section = snapshot.child("section").getValue(String.class);

                    if (admin != null && admin.equals("Yes")) {
                        Intent i = new Intent(getApplicationContext(), AdminHomeActivity.class);
                        startActivity(i);
                        finish();
                    }

                    if (cr != null && cr.equals("Yes")) {
                        rutine.setVisibility(View.VISIBLE);
                        test.setVisibility(View.VISIBLE);
                    }

                    welcome.setText("Welcome " + name);

                    // Subscribe to the class topic
                    subscribeToClassTopic(dept, series, section);
                } else {
                    welcome.setText("Name not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });

        rutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RoutineInputActivity.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Home.class));
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NoticeInputActivity.class));
            }
        });

        showRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define what happens when showRoutine button is clicked
                startActivity(new Intent(MainActivity.this, ShowRoutineActivity.class));
            }
        });
        showNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowNoticeActivity.class));
            }
        });

        // Initialize Firebase Messaging
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("MainActivity", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    Log.d("MainActivity", "FCM Token: " + token);

                    // Store the token in the user's profile in the database
                    if (user != null) {
                        reference.child(user.getUid()).child("fcmToken").setValue(token);
                    }
                });
    }

    private void subscribeToClassTopic(String dept, String series, String section) {
        String topic = "class_" + dept + "_" + series + "_" + section;
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("MainActivity", "Subscription to topic failed", task.getException());
                    } else {
                        Log.d("MainActivity", "Subscribed to topic: " + topic);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
        } else {
            String Uid = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference("users");
            reference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String admin = snapshot.child("admin").getValue(String.class);
                        if (admin != null && admin.equals("Yes")) {
                            Intent i = new Intent(getApplicationContext(), AdminHomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors.
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            case R.id.menu_change_password:
                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                return true;
            case R.id.menu_logout:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, login.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
