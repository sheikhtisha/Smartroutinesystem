package com.example.smartroutinesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    private EditText email;
    private EditText password;
    private Button saveToRealtimeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        saveToRealtimeDatabase = findViewById(R.id.saveToRealtimeDatabase);

        saveToRealtimeDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();


                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    // Assuming you have a User class representing your data structure


                    // Get the Firebase Realtime Database reference
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

                    // Push the user data to the database
                    databaseReference.child(userEmail.replace(".", ",")).setValue(user);

                    // Clear EditText fields after saving
                    email.setText("");
                    password.setText("");

                    // Optionally, you can show a toast message or handle success
                    Toast.makeText(MainActivity.this, "User data saved to Realtime Database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Please fill in both email and password fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
