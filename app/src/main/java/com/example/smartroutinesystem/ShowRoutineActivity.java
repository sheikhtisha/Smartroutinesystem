package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.util.List;

public class ShowRoutineActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef, rDatabaseRef;
    TextView welcomeTextView, deptTextView, seriesTextView, noClass;
    Spinner sectionSpinner, daySpinner, timeSpinner;
    Button submitButton;
    GridView gridView;
    GridAdapter gridAdapter;

    private String dept, series,sect;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routine);

        mAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("users");
        rDatabaseRef= FirebaseDatabase.getInstance().getReference("routine");
        gridView = findViewById(R.id.gridView);

        // Initialize TextViews
        welcomeTextView = findViewById(R.id.welcome);
        deptTextView = findViewById(R.id.deptTV);
        seriesTextView = findViewById(R.id.seriesTV);
        noClass=findViewById(R.id.NoClass);

        // Initialize Spinners
        sectionSpinner = findViewById(R.id.sectionSpinner);
        daySpinner = findViewById(R.id.daySpinner);
        timeSpinner = findViewById(R.id.timeSpinner);

        // Initialize Button
        submitButton = findViewById(R.id.button);



        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String Uid = user.getUid();
            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
            Query query = mDatabaseRef.child(Uid);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dept = dataSnapshot.child("department").getValue(String.class);
                        series = dataSnapshot.child("series").getValue(String.class);
                        sect = dataSnapshot.child("section").getValue(String.class);
                        String name=dataSnapshot.child("fullName").getValue(String.class);
                        position=secSel(sect);
                        sectionSpinner.setSelection(position);
                        welcomeTextView.setText("Welcome "+name);
                        deptTextView.setText("Department: "+dept);
                        seriesTextView.setText("Series: "+series);
                    } else {
                        // Handle the case where data does not exist
                        deptTextView.setText("Department: N/A");
                        seriesTextView.setText("Series: N/A");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    deptTextView.setText("Error: " + databaseError.getMessage());
                }
            });
        } else {
            // Handle user not authenticated
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }
        setupSpinners();
        setSpinnerListeners();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to fetch routine data
                getRoutine();
            }
        });

    }
    private void setupSpinners() {
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.Days,
                android.R.layout.simple_spinner_item
        );
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.AllTimes,
                android.R.layout.simple_spinner_item
        );
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        ArrayAdapter<CharSequence> secAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.sec,
                android.R.layout.simple_spinner_item
        );
        secAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(secAdapter);

    }

    private void setSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getRoutine();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        daySpinner.setOnItemSelectedListener(spinnerListener);
        timeSpinner.setOnItemSelectedListener(spinnerListener);
        sectionSpinner.setOnItemSelectedListener(spinnerListener);
    }

    private void getRoutine() {
        if (dept == null || series == null) {
            return;
        }

        String day = (String) daySpinner.getSelectedItem();
        String time = (String) timeSpinner.getSelectedItem();
        String section = (String) sectionSpinner.getSelectedItem();
        rDatabaseRef = FirebaseDatabase.getInstance().getReference("routine").child(dept)
                .child(series).child(section).child(day);
        if (time.equals("All Day")) {
            rDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<ViewData> items = new ArrayList<ViewData>();
                    for (DataSnapshot routineSnapshot : dataSnapshot.getChildren()) {
                        if (routineSnapshot.exists()) {
                            String course = routineSnapshot.child("course").getValue(String.class);
                            String room = routineSnapshot.child("room").getValue(String.class);
                            String teacher = routineSnapshot.child("name").getValue(String.class);
                            String time = routineSnapshot.child("time").getValue(String.class);

                            // Check if this routine entry meets your criteria
                            items.add(new ViewData(teacher, course, room, time));
                        }
                    }
                    if (items.isEmpty()) {
                        noClass.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.GONE);
                    } else {
                        gridView.setVisibility(View.VISIBLE);
                        gridAdapter = new GridAdapter(getApplicationContext(), items);
                        gridView.setAdapter(gridAdapter);
                        noClass.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        } else {
            String timeField = timeChild(time);
            List<ViewData> items = new ArrayList<ViewData>();
            rDatabaseRef.child(timeField).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot routineSnapshot) {
                    if (routineSnapshot.exists()) {
                        String course = routineSnapshot.child("course").getValue(String.class);
                        String room = routineSnapshot.child("room").getValue(String.class);
                        String teacher = routineSnapshot.child("name").getValue(String.class);
                        String time = routineSnapshot.child("time").getValue(String.class);

                        // Check if this routine entry meets your criteria
                        items.add(new ViewData(teacher, course, room, time));

                    }
                    if (items.isEmpty()) {
                        noClass.setText("No class Now!!!");
                        noClass.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.GONE);
                    } else {
                        gridView.setVisibility(View.VISIBLE);
                        gridAdapter = new GridAdapter(getApplicationContext(), items);
                        gridView.setAdapter(gridAdapter);
                        noClass.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                }
            });
        }
    }


    private String timeChild(String time) {
        switch (time) {
            case "8.00 AM":
                return "800";
            case "8.50 AM":
                return "850";
            case "9.40 AM":
                return "940";
            case "10.50 AM":
                return "1050";
            case "11.40 AM":
                return "1140";
            case "12.30 PM":
                return "1230";
            case "2.30 PM":
                return "1430";
            case "3.20 PM":
                return "1520";
            case "4.10 PM":
                return "1610";
            default:
                return "Unknown";
        }
    }
    private int secSel(String StrSection) {
        switch(StrSection){
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            default:
                return -1;
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
