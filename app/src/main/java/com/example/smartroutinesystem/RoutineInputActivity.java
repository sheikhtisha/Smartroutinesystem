package com.example.smartroutinesystem;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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

    private FirebaseAuth mAuth;
    private Spinner daySpinner, timeSpinner;
    private EditText teacherEditText, courseEditText, roomEditText;
    private TextView deptView, batchView, sectionView;
    private Button saveButton, deleteClassButton, deleteRoutineButton;
    private String dept, series, section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_input);

        mAuth = FirebaseAuth.getInstance();
        deptView = findViewById(R.id.deptTextView);
        batchView = findViewById(R.id.batchTextView);
        sectionView = findViewById(R.id.sectionTextView);
        daySpinner = findViewById(R.id.daySpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        teacherEditText = findViewById(R.id.teacherEditText);
        courseEditText = findViewById(R.id.courseEditText);
        roomEditText = findViewById(R.id.roomEditText);
        saveButton = findViewById(R.id.saveButton);
        deleteClassButton = findViewById(R.id.deleteClassButton);
        deleteRoutineButton = findViewById(R.id.deleteRoutineButton);

        setupSpinners();

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
                        section = dataSnapshot.child("section").getValue(String.class);

                        deptView.setText(String.format("Department: %s", dept));
                        batchView.setText(String.format("Series: %s", series));
                        sectionView.setText(String.format("Section: %s", section));

                        populateRoutine();
                    } else {
                        // Handle the case where data does not exist
                        deptView.setText("Department: N/A");
                        batchView.setText("Series: N/A");
                        sectionView.setText("Section: N/A");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    deptView.setText("Error: " + databaseError.getMessage());
                }
            });
        } else {
            // Handle user not authenticated
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRoutine();
            }
        });

        deleteClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               delClass();
            }
        });

        deleteRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delFullRoutine();
            }
        });

        setSpinnerListeners();
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
                R.array.time,
                android.R.layout.simple_spinner_item
        );
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);
    }

    private void setSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateRoutine();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        daySpinner.setOnItemSelectedListener(spinnerListener);
        timeSpinner.setOnItemSelectedListener(spinnerListener);
    }

    private void populateRoutine() {
        if (dept == null || series == null || section == null) {
            return;
        }

        String day = (String) daySpinner.getSelectedItem();
        String time = (String) timeSpinner.getSelectedItem();
        String timeField = timeChild(time);

        if (day == null || timeField.equals("Unknown")) {
            return;
        }

        DatabaseReference rDatabaseRef = FirebaseDatabase.getInstance().getReference("routine")
                .child(dept).child(series).child(section).child(day).child(timeField);
        rDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String course = snapshot.child("course").getValue(String.class);
                    String room = snapshot.child("room").getValue(String.class);
                    String teacher = snapshot.child("name").getValue(String.class);

                    teacherEditText.setText(teacher);
                    roomEditText.setText(room);
                    courseEditText.setText(course);
                } else {
                    teacherEditText.setText("");
                    roomEditText.setText("");
                    courseEditText.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void saveRoutine() {
        String day = (String) daySpinner.getSelectedItem();
        String time = (String) timeSpinner.getSelectedItem();
        String teacher = teacherEditText.getText().toString();
        String course = courseEditText.getText().toString();
        String room = roomEditText.getText().toString();
        String timeField = timeChild(time);

        if (validateInput(teacher, course, room)) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference routineRef = databaseRef.child("routine").child(dept).child(series).child(section);

            ViewData data = new ViewData(teacher, course, room, time);
            routineRef.child(day).child(timeField).setValue(data);

            Toast.makeText(RoutineInputActivity.this, "Routine saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RoutineInputActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String teacher, String course, String room) {
        return !teacher.isEmpty() && !course.isEmpty() && !room.isEmpty();
    }

    private void deleteClass() {
        String day = (String) daySpinner.getSelectedItem();
        String time = (String) timeSpinner.getSelectedItem();
        String timeField = timeChild(time);

        if (day == null || timeField.equals("Unknown")) {
            Toast.makeText(this, "Please select a valid day and time", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("routine")
                .child(dept).child(series).child(section).child(day).child(timeField);

        classRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RoutineInputActivity.this, "Class deleted", Toast.LENGTH_SHORT).show();
                teacherEditText.setText("");
                courseEditText.setText("");
                roomEditText.setText("");
            } else {
                Toast.makeText(RoutineInputActivity.this, "Failed to delete class", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFullRoutine() {
        DatabaseReference routineRef = FirebaseDatabase.getInstance().getReference("routine")
                .child(dept).child(series).child(section);

        routineRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RoutineInputActivity.this, "Routine deleted", Toast.LENGTH_SHORT).show();
                teacherEditText.setText("");
                courseEditText.setText("");
                roomEditText.setText("");
            } else {
                Toast.makeText(RoutineInputActivity.this, "Failed to delete routine", Toast.LENGTH_SHORT).show();
            }
        });
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
    private void delClass() {
        new AlertDialog.Builder(RoutineInputActivity.this)
                .setTitle("Remove this class")
                .setMessage("Are you sure to remove this Class?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteClass();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void delFullRoutine() {
        new AlertDialog.Builder(RoutineInputActivity.this)
                .setTitle("Remove Full Routine")
                .setMessage("Are you sure to remove full routine for this semester?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFullRoutine();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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
}
