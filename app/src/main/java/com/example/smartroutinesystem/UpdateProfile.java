package com.example.smartroutinesystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {

    private EditText fullNameEditText, departmentEditText, rollNumberEditText, seriesEditText, phoneEditText;
    private Button updateButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        fullNameEditText = findViewById(R.id.editTextFullName);
        departmentEditText = findViewById(R.id.editTextDepartment);
        rollNumberEditText = findViewById(R.id.editTextRollNumber);
        seriesEditText = findViewById(R.id.editTextSeries);
        phoneEditText =findViewById(R.id.editTextPhoneNumber);
        updateButton = findViewById(R.id.buttonUpdate);

        // Fetch current user's data and populate EditText fields
        populateEditTextFields();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void populateEditTextFields() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            mDatabaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String department = dataSnapshot.child("department").getValue(String.class);
                        String rollNumber = dataSnapshot.child("rollNumber").getValue(String.class);
                        String series = dataSnapshot.child("series").getValue(String.class);
                        String phone= dataSnapshot.child("phoneNumber").getValue(String.class);

                        // Set the retrieved values to EditText fields
                        fullNameEditText.setText(fullName);
                        departmentEditText.setText(department);
                        rollNumberEditText.setText(rollNumber);
                        seriesEditText.setText(series);
                        phoneEditText.setText(phone);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Toast.makeText(UpdateProfile.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            String fullName = fullNameEditText.getText().toString().trim();
            String department = departmentEditText.getText().toString().trim();
            String rollNumber = rollNumberEditText.getText().toString().trim();
            String series = seriesEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            // Check if roll number already exists for another user
            mDatabaseRef.orderByChild("rollNumber").equalTo(rollNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean rollNumberExists = false;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (!snapshot.getKey().equals(userId)) {
                            rollNumberExists = true;
                            break;
                        }
                    }

                    if (rollNumberExists) {
                        // Roll number exists for another user
                        Toast.makeText(UpdateProfile.this, "Roll number already exists for another user.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Roll number is unique or belongs to the current user
                        // Update user's profile in the RTDB
                        mDatabaseRef.child(userId).child("fullName").setValue(fullName);
                        mDatabaseRef.child(userId).child("department").setValue(department);
                        mDatabaseRef.child(userId).child("rollNumber").setValue(rollNumber);
                        mDatabaseRef.child(userId).child("series").setValue(series);
                        mDatabaseRef.child(userId).child("phoneNumber").setValue(phone);

                        // Show a toast message to indicate successful update
                        Toast.makeText(UpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Toast.makeText(UpdateProfile.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User is not authenticated
            Toast.makeText(UpdateProfile.this, "User not authenticated", Toast.LENGTH_SHORT).show();
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
