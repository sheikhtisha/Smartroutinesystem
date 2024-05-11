package com.example.smartroutinesystem;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoutineInputActivity extends AppCompatActivity {

    private Spinner deptSpinner, batchSpinner, sectionSpinner, daySpinner, timeSpinner;
    private EditText locationEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_input);

        deptSpinner = findViewById(R.id.deptSpinner);
        batchSpinner = findViewById(R.id.batchSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        daySpinner = findViewById(R.id.daySpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        locationEditText = findViewById(R.id.locationEditText);
        saveButton = findViewById(R.id.saveButton);


    // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.Days,
                android.R.layout.simple_spinner_item
        );
    // Specify the layout to use when the list of choices appears.
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> batchAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.Series,
                android.R.layout.simple_spinner_item
        );
        batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.Depts,
                android.R.layout.simple_spinner_item
        );
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.time,
                android.R.layout.simple_spinner_item
        );
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> secAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.sec,
                android.R.layout.simple_spinner_item
        );
        secAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        daySpinner.setAdapter(dayAdapter);

        deptSpinner.setAdapter(deptAdapter);
        batchSpinner.setAdapter(batchAdapter);
        sectionSpinner.setAdapter(secAdapter);
        timeSpinner.setAdapter(timeAdapter);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRoutine();
            }
        });
    }

    private void saveRoutine() {
        String dept = deptSpinner.getSelectedItem().toString();
        String batch = batchSpinner.getSelectedItem().toString();
        String section = sectionSpinner.getSelectedItem().toString();
        String day = daySpinner.getSelectedItem().toString();
        String time = timeSpinner.getSelectedItem().toString();
        String location = locationEditText.getText().toString();

        // Get a reference to the Firebase RTDB
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Assuming "routines" is the root node where routines are stored
        DatabaseReference routineRef = databaseRef.child("routine");

        // Generate a unique key for the routine
        String routineId = routineRef.push().getKey();

        // Create a Routine object
        Routine routine = new Routine(dept, batch, section, day, time, location);

        // Save the routine to Firebase RTDB
        if (routineId != null) {
            routineRef.child(routineId).setValue(routine);
        }
    }

}
