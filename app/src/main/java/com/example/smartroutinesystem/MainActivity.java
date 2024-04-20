package com.example.smartroutinesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    private EditText email;
    private EditText password;
    private Button saveToRealtimeDatabase;
    private Fibasedatabase firebasedatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth= FirebaseAuth.getInstance();
        button= findViewById(R.id.logout);
        textView= findViewById(R.id.user_details);
        user= auth.getCurrentUser();
        if (user == null){
            Intent intent= new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }
        else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });

        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        saveToRealtimeDatabase= findViewById(R.id.saveToRealtimeDatabase);

        saveToRealtimeDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}


