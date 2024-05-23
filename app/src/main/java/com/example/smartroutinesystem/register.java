package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

public class register extends AppCompatActivity {
    EditText editTextEmail, editTextPassword, editTextConfirmPassword, editTextFullName, editTextPhoneNumber, editTextRollNumber, editTextSeries;
    Spinner departmentSpinner, sectionSpinner;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);
        editTextFullName = findViewById(R.id.fullName);
        editTextPhoneNumber = findViewById(R.id.phoneNumber);
        editTextRollNumber = findViewById(R.id.rollNumber);
        editTextSeries = findViewById(R.id.series);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        // Spinner dropdown elements
        String[] departments = {"CSE", "EEE", "CE", "ME", "ECE", "ETE", "MTE"};

        // Creating adapter for spinner
        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        // Drop down layout style - list view with radio button
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        departmentSpinner.setAdapter(departmentAdapter);

        ArrayAdapter<CharSequence> secAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.sec,
                android.R.layout.simple_spinner_item
        );
        secAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sectionSpinner.setAdapter(secAdapter);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                String fullName = editTextFullName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String rollNumber = editTextRollNumber.getText().toString().trim();
                String series = editTextSeries.getText().toString().trim();
                String department = departmentSpinner.getSelectedItem().toString();
                String selectedSection = sectionSpinner.getSelectedItem().toString();
                String cr = "NO";
                String admin = "NO";

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(rollNumber) || TextUtils.isEmpty(series)) {
                    Toast.makeText(register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                // Determine the section based on the last three digits of the roll number
                int lastThreeDigits = Integer.parseInt(rollNumber.substring(rollNumber.length() - 3));
                String determinedSection;
                if (lastThreeDigits >= 1 && lastThreeDigits <= 60) {
                    determinedSection = "A";
                } else if (lastThreeDigits >= 61 && lastThreeDigits <= 120) {
                    determinedSection = "B";
                } else if (lastThreeDigits >= 121 && lastThreeDigits <= 181) {
                    determinedSection = "C";
                } else {
                    Toast.makeText(register.this, "Invalid roll number", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                // Check if the selected section matches the determined section
                if (!selectedSection.equals(determinedSection)) {
                    Toast.makeText(register.this, "Selected section does not match the roll number", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                // Check if a user with the same roll number already exists
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                databaseReference.orderByChild("rollNumber").equalTo(rollNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(register.this, "User with this roll number already exists", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            // Hash the password
                            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                String userId = user.getUid();
                                                // Create a user object with all the details
                                                User userData = new User(email, fullName, phoneNumber, rollNumber, series, department, selectedSection, cr, admin);

                                                // Save the user data to Firebase Realtime Database
                                                databaseReference.child(userId).setValue(userData);

                                                Toast.makeText(register.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), login.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(register.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
