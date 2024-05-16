package com.example.smartroutinesystem;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoutineInputActivity extends AppCompatActivity {

    private Spinner deptSpinner, batchSpinner, sectionSpinner, daySpinner, timeSpinner;
    private EditText teacherEditText, courseEditText, roomEditText;
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
        String teacher = teacherEditText.getText().toString();
        String course = courseEditText.getText().toString();
        String room = roomEditText.getText().toString();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference routineRef = databaseRef.child("routine");
        String routineId = routineRef.push().getKey();

        Routine routine = new Routine(dept, batch, section, day, time, teacher, course, room);

        if (routineId != null) {
            routineRef.child(routineId).setValue(routine);
        }
    }
}
