package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    TextView textView1, textView2, textView3, textView4,textView5;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize DatabaseReference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");


        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);
        textView3 = findViewById(R.id.tv3);
        textView4 = findViewById(R.id.tv4);
        textView5=findViewById(R.id.tv5);
        btn1= findViewById(R.id.btn_change);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ProfileActivity.this,UpdateProfile.class);
                startActivity(i);
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            // Query to fetch the data of the current user
            Query query = mDatabaseRef.child(userId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve the current user's data
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String department = dataSnapshot.child("department").getValue(String.class);
                        String rollNumber = dataSnapshot.child("rollNumber").getValue(String.class);
                        String series = dataSnapshot.child("series").getValue(String.class);
                        String section=dataSnapshot.child("section").getValue(String.class);

                        // Display the retrieved data
                        textView1.setText("Name: " + fullName);
                        textView2.setText("Department: " + department);
                        textView3.setText("Roll Number: " + rollNumber);
                        textView4.setText("Series: " + series);
                        textView5.setText("Section: "+section);
                    } else {
                        // Current user not found in the database
                        textView1.setText("User data not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    textView1.setText("Error: " + databaseError.getMessage());
                }
            });
        } else {
            // User is not signed in
            textView1.setText("User not signed in");
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
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            case R.id.menu_change_password:
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                return true;
            case R.id.menu_logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
                return true;
            // Add more cases for other options like settings, etc.
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
