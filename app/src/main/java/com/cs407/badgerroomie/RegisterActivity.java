package com.cs407.badgerroomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private String gender, ethnicity, roommate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //spinner for gender
        Spinner spinnerGender = (Spinner) findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(
                this,
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        );
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        //spinner for ethnicity
        Spinner spinnerEthnicity = (Spinner) findViewById(R.id.ethnicity_spinner);
        ArrayAdapter<CharSequence> adapterEthnicity = ArrayAdapter.createFromResource(
                this,
                R.array.ethnicity_array,
                android.R.layout.simple_spinner_item
        );
        adapterEthnicity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEthnicity.setAdapter(adapterEthnicity);

        //spinner for preferred roommates
        Spinner spinnerRoommate = (Spinner) findViewById(R.id.roommate_spinner);
        ArrayAdapter<CharSequence> adapterRoommate = ArrayAdapter.createFromResource(
                this,
                R.array.roommate_array,
                android.R.layout.simple_spinner_item
        );
        adapterRoommate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoommate.setAdapter(adapterRoommate);

        //store spinner selections
        gender = spinnerGender.getSelectedItem().toString();
        ethnicity = spinnerEthnicity.getSelectedItem().toString();
        roommate = spinnerRoommate.getSelectedItem().toString();

        editTextName = findViewById(R.id.editText_name);
        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);

        progressBar = findViewById(R.id.progressBar);
    }

    public void onClickButtonRegister(View view){
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
            editTextName.setError("Full name required");
            editTextName.requestFocus();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            registerUser(name, email, password, gender, ethnicity, roommate);
        }
    }

    //store in Firebase
    private void registerUser(String name, String email, String password, String gender, String ethnicity, String roommate) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //go to home activity
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    //prevents user from returning to register activity when pressing back
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}