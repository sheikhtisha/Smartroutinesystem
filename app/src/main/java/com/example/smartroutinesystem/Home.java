package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.List;

public class Home extends AppCompatActivity {

    private TextView textView1,textView2;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef, rDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // If no user is logged in, redirect to the login activity
            startActivity(new Intent(Home.this, login.class));
            finish();
            return; // Exit onCreate to prevent further execution
        }

        // Initialize views
        textView1 = findViewById(R.id.tv1);
        textView2=findViewById(R.id.tv2);

        // Get the current day of the week
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Convert integer day of the week to string representation
        String currentDay = getDayOfWeekString(currentDayOfWeek);
        // Firebase database references
        String uId = user.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(uId);
        rDatabaseRef = FirebaseDatabase.getInstance().getReference("routine");

        textView1.setText("Today is "+currentDay);
        // Query user data
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String depart, seri, sec;
                    depart = snapshot.child("department").getValue(String.class);
                    seri = snapshot.child("series").getValue(String.class);
                    sec = snapshot.child("section").getValue(String.class);
                    textView2.setText("Routine for "+depart+" "+ seri+" section "+sec);

                    rDatabaseRef = FirebaseDatabase.getInstance().getReference("routine").child(depart).child(seri).child(sec).child(currentDay);

                    rDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<ViewData> items = new ArrayList<ViewData>();
                            for (DataSnapshot routineSnapshot : dataSnapshot.getChildren()) {
                               if (routineSnapshot.exists()){
                                   String course = routineSnapshot.child("course").getValue(String.class);
                                    String room = routineSnapshot.child("room").getValue(String.class);
                                    String teacher = routineSnapshot.child("name").getValue(String.class);
                                    String time = routineSnapshot.child("time").getValue(String.class);

                                    // Check if this routine entry meets your criteria
                                    items.add(new ViewData(teacher, course, room,time));}
                            }
                            if (items.isEmpty())
                            {
                                textView1.setText("NO Class Today");
                            }
                            else
                            {
                                RecyclerView recyclerView=findViewById(R.id.recyclerview);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                                recyclerView.setAdapter(new RoutineAdapter(getApplicationContext(),items));
                            }
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

    // Helper method to convert integer day of the week to string representation
    private String getDayOfWeekString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "Unknown";
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
