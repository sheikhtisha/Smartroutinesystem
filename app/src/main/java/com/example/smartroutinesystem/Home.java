package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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


import java.util.ArrayList;
import java.util.Calendar;

public class Home extends AppCompatActivity {

    TextView textView1, textView2, textView3, textView4;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef, rDatabaseRef;
    String dept, series, section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        rDatabaseRef = FirebaseDatabase.getInstance().getReference("routine");
        textView1=findViewById(R.id.tv1);
        // Get the current day of the week (1 for Sunday, 2 for Monday, ..., 7 for Saturday)
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

// Convert integer day of the week to string representation
        String day;
        switch (currentDayOfWeek) {
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
            default:
                day = "Unknown";
        }
        textView1.setText(day);
        Query q1 = mDatabaseRef.child(uId);

        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dept = snapshot.child("department").getValue(String.class);
                    series = snapshot.child("series").getValue(String.class);
                    section = snapshot.child("section").getValue(String.class);

                    // Query the routine table for the current day's routine
                    Query routineQuery = rDatabaseRef.orderByChild("dayOfWeek").equalTo(currentDayOfWeek)
                            .orderByChild("department").equalTo(dept)
                            .orderByChild("series").equalTo(series)
                            .orderByChild("section").equalTo(section);

                    routineQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Retrieve the routine for the current day and populate the RecyclerView
                            ArrayList<ViewData> routineList = new ArrayList<>();
                            for (DataSnapshot routineSnapshot : dataSnapshot.getChildren()) {
                                // Parse routine data and add to routineList
                                String name = routineSnapshot.child("name").getValue(String.class);
                                String course = routineSnapshot.child("course").getValue(String.class);
                                String room = routineSnapshot.child("room").getValue(String.class);
                                routineList.add(new ViewData(name, course, room));
                            }
                            // Populate RecyclerView with routineList using the ViewAdapter
                            RecyclerView recyclerView = findViewById(R.id.recyclerview);
                            ViewAdapter adapter = new ViewAdapter(Home.this, routineList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(Home.this, login.class));
            finish();
        }
    }
}
