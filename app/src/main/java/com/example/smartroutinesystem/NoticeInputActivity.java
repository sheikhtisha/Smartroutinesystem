package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoticeInputActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private EditText editTextNoticeTitle;
    private EditText editTextNoticeBody;
    private Button buttonSubmit;

    private String dept, series, section,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_input);

        // Initialize EditText and Button fields
        editTextNoticeTitle = findViewById(R.id.editTextNoticeTitle);
        editTextNoticeBody = findViewById(R.id.editTextNoticeBody);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String Uid = user.getUid();
            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
            Query query = mDatabaseRef.child(Uid);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String cr = dataSnapshot.child("cr").getValue(String.class);
                    dept = dataSnapshot.child("department").getValue(String.class);
                    series = dataSnapshot.child("series").getValue(String.class);
                    section = dataSnapshot.child("section").getValue(String.class);
                    name = dataSnapshot.child("fullName").getValue(String.class);

                    if (cr.equals("No")) {
                        startActivity(new Intent(getApplicationContext(), MakeCrActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        } else {
            // Handle user not authenticated
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeInput();
            }
        });
    }

    private void noticeInput() {
        String title = editTextNoticeTitle.getText().toString().trim();
        String body = editTextNoticeBody.getText().toString().trim();

        if (title.isEmpty() || body.isEmpty()) {
            Toast.makeText(NoticeInputActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference nReference = FirebaseDatabase.getInstance().getReference("notices").child(dept).child(series).child(section);
        String noticeId = nReference.push().getKey();

        Notice notice = new Notice(name, title, body);
        nReference.child(noticeId).setValue(notice).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                sendNotification(title, body);
                Toast.makeText(NoticeInputActivity.this, "Notice submitted successfully", Toast.LENGTH_SHORT).show();
                // Clear the input fields
                editTextNoticeTitle.setText("");
                editTextNoticeBody.setText("");
            } else {
                Toast.makeText(NoticeInputActivity.this, "Failed to submit notice. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(String title, String body) {
        String topic = "/topics/class_" + dept + "_" + series + "_" + section;

        try {
            JSONObject json = new JSONObject();
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", body);

            json.put("to", topic);
            json.put("notification", notification);

            callApi(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/v1/projects/smart-routine-system-ea7a1/messages:send";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e("FCM", "User is not authenticated");
            return;
        }

        user.getIdToken(true).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String idToken = task.getResult().getToken();
                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Authorization", "Bearer " + idToken)
                        .addHeader("Content-Type", "application/json")
                        .build();

                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Log.d("FCM", "Successfully sent notification");
                        } else {
                            Log.d("FCM", "Failed to send notification");
                        }
                    }
                });
            } else {
                Log.e("FCM", "Failed to get user token", task.getException());
            }
        });
    }

}
