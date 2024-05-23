package com.example.smartroutinesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputEditText editTextCurrentPassword, editTextNewPassword, editTextConfirmNewPassword;
    Button buttonChangePassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        editTextCurrentPassword = findViewById(R.id.current_password);
        editTextNewPassword = findViewById(R.id.new_password);
        editTextConfirmNewPassword = findViewById(R.id.confirm_new_password);
        buttonChangePassword = findViewById(R.id.btn_change_password);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String currentPassword = editTextCurrentPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmNewPassword = editTextConfirmNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, "Enter current password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Enter new password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(this, "New Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, "Error updating password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Incorrect Current Password", Toast.LENGTH_SHORT).show();
                    }
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
