package com.example.smartroutinesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText email, password, fullName, phoneNumber, rollNumber, series;
    Spinner departmentSpinner;
    Button saveToRealtimeDatabase, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fullName = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        rollNumber = findViewById(R.id.rollNumber);
        series = findViewById(R.id.series);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        saveToRealtimeDatabase = findViewById(R.id.saveToRealtimeDatabase);
        logoutButton = findViewById(R.id.logout);

        // Set values for the spinner
        String[] departments = {"CSE", "EEE", "CE", "ME", "ECE", "ETE", "MTE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter);

        // Setup click listener for save button
        saveToRealtimeDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String userFullName = fullName.getText().toString().trim();
                String userPhoneNumber = phoneNumber.getText().toString().trim();
                String userRollNumber = rollNumber.getText().toString().trim();
                String userSeries = series.getText().toString().trim();
                String userDepartment = departmentSpinner.getSelectedItem().toString();

                // Check if any field is empty
                if (userEmail.isEmpty() || userPassword.isEmpty() || userFullName.isEmpty() || userPhoneNumber.isEmpty() ||
                        userRollNumber.isEmpty() || userSeries.isEmpty() || userDepartment.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the Firebase Realtime Database reference
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

                // Create a new User object with the input data
                User user = new User(userEmail, userPassword, userFullName, userPhoneNumber, userRollNumber, userSeries, userDepartment);

                // Generate a unique key for the new user
                String userId = databaseReference.push().getKey();

                // Write the user data to the database under the unique key
                databaseReference.child(userId).setValue(user);

                // Clear EditText fields after saving
                email.setText("");
                password.setText("");
                fullName.setText("");
                phoneNumber.setText("");
                rollNumber.setText("");
                series.setText("");

                // Navigate to the home activity
                Intent intent = new Intent(MainActivity.this, home.class);
                startActivity(intent);
            }
        });

        // Setup click listener for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the current user
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login activity
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
