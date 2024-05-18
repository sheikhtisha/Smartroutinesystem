package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView IdShow, welcome;
    Button rutine,home,test;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IdShow=findViewById(R.id.userIdCheck);
        welcome=findViewById(R.id.user_details);
        auth = FirebaseAuth.getInstance();
        rutine=findViewById(R.id.btn_rtn);
        home=findViewById(R.id.btn_home);
        test=findViewById(R.id.btn_test);
        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        String Uid= user.getUid();
        reference=FirebaseDatabase.getInstance().getReference("users");
        reference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name= snapshot.child("fullName").getValue(String.class);
                    welcome.setText("Welcome "+name);
                }
                else {
                    welcome.setText("Name not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        rutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RoutineInputActivity.class);
                startActivity(intent);
                finish();
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
                startActivity(new Intent(MainActivity.this, test.class));
            }
        });
    }
    String userid;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
//        userid= user.getUid();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
        }
//        IdShow.setText(userid);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, login.class));
                finish();
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            // Add more cases for other options like settings profile, etc.
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
