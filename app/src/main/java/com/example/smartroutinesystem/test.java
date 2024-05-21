package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference rDatabase, mDatabase;
    TextView textView;
    EditText TeacherEdit,CourseEdit,RoomEdit;
    Button set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mDatabase= FirebaseDatabase.getInstance().getReference("users");
        rDatabase = FirebaseDatabase.getInstance().getReference("routine");
        textView = findViewById(R.id.textView1);
        TeacherEdit=findViewById(R.id.TeacherEditView);
        CourseEdit=findViewById(R.id.ClassEditView);
        RoomEdit=findViewById(R.id.RoomEditText);
        set= findViewById(R.id.btn_ok);
        mAuth=FirebaseAuth.getInstance();
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacher=TeacherEdit.getText().toString();
                String course= CourseEdit.getText().toString();
                String Room= RoomEdit.getText().toString();
                ViewData data= new ViewData(teacher,course,Room,"8 a.m.");
                rDatabase.child("CSE").child("19").child("A").child("Saturday").setValue(data);
                Toast.makeText(getApplicationContext(),"Routine saved", Toast.LENGTH_SHORT).show();
            }
        });

    }

}



