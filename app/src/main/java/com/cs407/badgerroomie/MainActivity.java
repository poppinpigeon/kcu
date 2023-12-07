package com.cs407.badgerroomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPassword;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLoginEmail = findViewById(R.id.editText_email_login);
        editTextLoginPassword = findViewById(R.id.editText_password_login);
        progressBar = findViewById(R.id.progressBar_signIn);

        authProfile = FirebaseAuth.getInstance();
    }

    public void onClickButtonSignIn(View view){
        String loginEmail = editTextLoginEmail.getText().toString();
        String loginPassword = editTextLoginPassword.getText().toString();

        if(TextUtils.isEmpty(loginEmail)){
            Toast.makeText(MainActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
            editTextLoginEmail.setError("Email is required");
            editTextLoginEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(loginPassword)){
            Toast.makeText(MainActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
            editTextLoginPassword.setError("Password is required");
            editTextLoginPassword.requestFocus();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            loginUser(loginEmail, loginPassword);
        }
    }

    private void loginUser(String e, String p) {
        authProfile.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Signed in successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void onClickTextRegister(View view){
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}