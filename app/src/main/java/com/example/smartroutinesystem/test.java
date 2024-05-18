package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference rDatabase;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        rDatabase = FirebaseDatabase.getInstance().getReference("routine");
        textView = findViewById(R.id.textView1);


        // Fetch all routine entries
        rDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder teachers = new StringBuilder();
                List<ViewData>items=new ArrayList<ViewData>();
                for (DataSnapshot routineSnapshot : dataSnapshot.getChildren()) {
                    String batch = routineSnapshot.child("batch").getValue(String.class);
                    String course = routineSnapshot.child("course").getValue(String.class);
                    String day = routineSnapshot.child("day").getValue(String.class);
                    String dept = routineSnapshot.child("dept").getValue(String.class);
                    String room = routineSnapshot.child("room").getValue(String.class);
                    String section = routineSnapshot.child("section").getValue(String.class);
                    String teacher = routineSnapshot.child("teacher").getValue(String.class);
                    String time = routineSnapshot.child("time").getValue(String.class);

                    // Check if this routine entry meets your criteria
                    if (dept.equals("CSE") && batch.equals("19") && section.equals("A") && day.equals("Saturday")) {
                        items.add(new ViewData(teacher,course,room));
                    }
                }
                RecyclerView recyclerView=findViewById(R.id.testView);

                recyclerView.setLayoutManager(new LinearLayoutManager(test.this));
                recyclerView.setAdapter(new RoutineAdapter(getApplicationContext(),items));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}



