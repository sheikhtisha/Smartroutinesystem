package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminHomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private Button btnMakeCr;
    private Button btnRemoveCr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            // If no user is logged in, redirect to the login activity
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
            return; // Exit onCreate to prevent further execution
        }

        btnMakeCr = findViewById(R.id.btn_make_cr);
        btnRemoveCr = findViewById(R.id.btn_remove_cr);


        btnMakeCr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Make CR button click
                startActivity(new Intent(AdminHomeActivity.this, MakeCrActivity.class));
            }
        });

        btnRemoveCr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Remove CR button click
                startActivity(new Intent(AdminHomeActivity.this, RemoveCrActivity.class));
            }
        });


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
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
