package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView IdShow;
    Button rutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IdShow=findViewById(R.id.userIdCheck);
        auth = FirebaseAuth.getInstance();
        rutine=findViewById(R.id.btn_rtn);

        rutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RoutineInputActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    String userid;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        userid= user.getUid();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
        }
        IdShow.setText(userid);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, login.class));
                finish();
                return true;
            // Add more cases for other options like settings profile, etc.
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
