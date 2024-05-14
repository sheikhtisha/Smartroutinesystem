package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    TextView textView1, textView2, textView3, textView4;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize DatabaseReference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);
        textView3 = findViewById(R.id.tv3);
        textView4 = findViewById(R.id.tv4);

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

                        // Display the retrieved data
                        textView1.setText("Full Name: " + fullName);
                        textView2.setText("Department: " + department);
                        textView3.setText("Roll Number: " + rollNumber);
                        textView4.setText("Series: " + series);
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
}
