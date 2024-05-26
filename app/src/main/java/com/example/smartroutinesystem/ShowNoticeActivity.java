package com.example.smartroutinesystem;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowNoticeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private String dept, series, section;
    RecyclerView recyclerView;
    NoticeAdapter noticeAdapter;
    ArrayList<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notice);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String Uid = user.getUid();
            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
            Query query = mDatabaseRef.child(Uid);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dept = dataSnapshot.child("department").getValue(String.class);
                    series = dataSnapshot.child("series").getValue(String.class);
                    section = dataSnapshot.child("section").getValue(String.class);
                    // Now fetch notices for this department, series, and section
                    fetchNotices();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }

        // Initialize noticeList
        noticeList = new ArrayList<>();

        // Initialize RecyclerView and set layout manager
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchNotices() {
        DatabaseReference nReference = FirebaseDatabase.getInstance().getReference("notices")
                .child(dept).child(series).child(section);
        nReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Check for null values before accessing
                    String name = snapshot.child("sender").getValue(String.class);
                    String title = snapshot.child("title").getValue(String.class);
                    String body = snapshot.child("body").getValue(String.class);

                    if (name != null && title != null && body != null) {
                        Notice notice = new Notice(name, title, body);
                        noticeList.add(notice);
                    }
                }
                // Initialize noticeAdapter and set adapter for RecyclerView
                noticeAdapter = new NoticeAdapter(noticeList);
                recyclerView.setAdapter(noticeAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
