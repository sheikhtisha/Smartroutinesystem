package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MakeCrActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private EditText editTextRollNo;
    private Button buttonSearch;
    private TextView textViewStudentData, textView1, textView2, textView3, textView4, textView5;
    private Button buttonMakeCR;

    private DatabaseReference databaseReference;
    String uid;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_cr);

        mAuth = FirebaseAuth.getInstance();
        editTextRollNo = findViewById(R.id.editTextRollNo);
        buttonSearch = findViewById(R.id.buttonSearch);
        textViewStudentData = findViewById(R.id.textViewStudentData);

        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);
        textView3 = findViewById(R.id.tv3);
        textView4 = findViewById(R.id.tv4);
        textView5 = findViewById(R.id.tv5);
        buttonMakeCR = findViewById(R.id.buttonMakeCR);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = mAuth.getCurrentUser();
        String Uid = user.getUid();
        databaseReference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("fullName").getValue(String.class);
                    String admin = snapshot.child("admin").getValue(String.class);
                    if (admin != null && admin.equals("No")) {
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStudent();
            }
        });

        buttonMakeCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    private void searchStudent() {
        String rollNo = editTextRollNo.getText().toString().trim();
        if (rollNo.isEmpty()) {
            Toast.makeText(this, "Please enter a roll number", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0; // Reset count before searching
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.exists()) {
                        String roll = userSnapshot.child("rollNumber").getValue().toString();
                        if (roll.equals(rollNo)) {
                            count = 1;
                            uid = userSnapshot.getKey();
                            String fullName = userSnapshot.child("fullName").getValue(String.class);
                            String department = userSnapshot.child("department").getValue(String.class);
                            String rollNumber = userSnapshot.child("rollNumber").getValue(String.class);
                            String series = userSnapshot.child("series").getValue(String.class);
                            String section = userSnapshot.child("section").getValue(String.class);
                            String cr = userSnapshot.child("cr").getValue(String.class);
                            // Display the retrieved data
                            textView1.setText("Name: " + fullName);
                            textView2.setText("Department: " + department);
                            textView3.setText("Roll Number: " + rollNumber);
                            textView4.setText("Series: " + series);
                            textView5.setText("Section: " + section);
                            textViewStudentData.setText("CR: " + cr);
                            buttonMakeCR.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (count == 0) {
                    textView1.setText("Student not found");
                    buttonMakeCR.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MakeCrActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Make CR")
                .setMessage("Are you sure you want to make this student a CR?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        makeStudentCR();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void makeStudentCR() {
        if (uid != null && !uid.isEmpty()) {
            databaseReference.child(uid).child("cr").setValue("Yes")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            textViewStudentData.setText("CR: Yes");
                            Toast.makeText(MakeCrActivity.this, "Student made CR successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MakeCrActivity.this, "Failed to make student CR", Toast.LENGTH_SHORT).show();
                        }
                    });
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
