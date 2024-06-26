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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

public class RemoveCrActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Spinner deptSpinner, seriesSpinner, sectionSpinner;
    private Button searchCrButton;
    private RecyclerView crRecyclerView;
    private CrAdapter crAdapter;
    private List<Cr> crList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_cr);

        mAuth = FirebaseAuth.getInstance();
        deptSpinner = findViewById(R.id.deptSpinner);
        seriesSpinner = findViewById(R.id.seriesSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        searchCrButton = findViewById(R.id.searchCrButton);
        crRecyclerView = findViewById(R.id.crRecyclerView);

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

        setupSpinners();
        setupRecyclerView();

        searchCrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCr();
            }
        });
    }

    private void setupSpinners() {
        // Setup department spinner
        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(this, R.array.Depts, android.R.layout.simple_spinner_item);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptSpinner.setAdapter(deptAdapter);

        // Setup series spinner
        ArrayAdapter<CharSequence> seriesAdapter = ArrayAdapter.createFromResource(this, R.array.Series, android.R.layout.simple_spinner_item);
        seriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seriesSpinner.setAdapter(seriesAdapter);

        // Setup section spinner
        ArrayAdapter<CharSequence> sectionAdapter = ArrayAdapter.createFromResource(this, R.array.sec, android.R.layout.simple_spinner_item);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(sectionAdapter);
    }

    private void setupRecyclerView() {
        crList = new ArrayList<>();
        crAdapter = new CrAdapter(this, crList);
        crRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        crRecyclerView.setAdapter(crAdapter);
    }

    private void searchCr() {
        String dept = deptSpinner.getSelectedItem().toString();
        String series = seriesSpinner.getSelectedItem().toString();
        String section = sectionSpinner.getSelectedItem().toString();

        Query query = databaseReference.orderByChild("cr").equalTo("Yes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                crList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String studentDept = snapshot.child("department").getValue(String.class);
                    String studentSeries = snapshot.child("series").getValue(String.class);
                    String studentSection = snapshot.child("section").getValue(String.class);

                    if (studentDept.equals(dept) && studentSeries.equals(series) && studentSection.equals(section)) {
                        String uid = snapshot.getKey();
                        String name = snapshot.child("fullName").getValue(String.class);
                        String rollNumber = snapshot.child("rollNumber").getValue(String.class);

                        Cr cr = new Cr(uid, name, rollNumber, studentDept, studentSeries, studentSection);
                        crList.add(cr);
                    }
                }
                crAdapter.notifyDataSetChanged();

                if (crList.isEmpty()) {
                    Toast.makeText(RemoveCrActivity.this, "No CR found for the selected department, series, and section.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RemoveCrActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
}
