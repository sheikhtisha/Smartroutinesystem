package com.example.smartroutinesystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RoutineInputActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Spinner  daySpinner, timeSpinner;
    private EditText teacherEditText, courseEditText, roomEditText;
    private TextView deptView, batchView, sectionView;
    private Button saveButton;
    String dept,series,section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_input);
        mAuth=FirebaseAuth.getInstance();
        deptView=findViewById(R.id.deptTextView);
        batchView=findViewById(R.id.batchTextView);
        sectionView=findViewById(R.id.sectionTextView);
        daySpinner = findViewById(R.id.daySpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        teacherEditText = findViewById(R.id.teacherEditText);
        courseEditText = findViewById(R.id.courseEditText);
        roomEditText = findViewById(R.id.roomEditText);
        saveButton = findViewById(R.id.saveButton);

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.Days,
                android.R.layout.simple_spinner_item
        );
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.time,
                android.R.layout.simple_spinner_item
        );
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        daySpinner.setAdapter(dayAdapter);
        timeSpinner.setAdapter(timeAdapter);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String Uid=user.getUid();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        // Query to fetch the data of the current user
        Query query = mDatabaseRef.child(Uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the current user's data

                    dept= dataSnapshot.child("department").getValue(String.class);
                    series = dataSnapshot.child("series").getValue(String.class);
                    section=dataSnapshot.child("section").getValue(String.class);

                    // Display the retrieved data
                    deptView.setText("Department: " + dept);
                    batchView.setText("Series: " + series);
                    sectionView.setText("Section "+section);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                deptView.setText("Error: " + databaseError.getMessage());
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRoutine();
            }
        });
    }

    private void saveRoutine() {

        String day = daySpinner.getSelectedItem().toString();
        String time = timeSpinner.getSelectedItem().toString();
        String teacher = teacherEditText.getText().toString();
        String course = courseEditText.getText().toString();
        String room = roomEditText.getText().toString();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference routineRef = databaseRef.child("routine");
        String routineId = routineRef.push().getKey();

        Routine routine = new Routine(dept, series, section, day, time, teacher, course, room);

        if (routineId != null) {
            routineRef.child(routineId).setValue(routine);
            Toast.makeText(RoutineInputActivity.this, "Routine saved", Toast.LENGTH_SHORT).show();
        }
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
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            // Add more cases for other options like settings profile, etc.
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}