package com.cs407.badgerroomie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private String gender, ethnicity, roommate;
    private static final String TAG = "RegisterActivity";

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

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = spinnerGender.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        //spinner for ethnicity
        Spinner spinnerEthnicity = (Spinner) findViewById(R.id.ethnicity_spinner);
        ArrayAdapter<CharSequence> adapterEthnicity = ArrayAdapter.createFromResource(
                this,
                R.array.ethnicity_array,
                android.R.layout.simple_spinner_item
        );
        adapterEthnicity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEthnicity.setAdapter(adapterEthnicity);

        spinnerEthnicity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ethnicity = spinnerEthnicity.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner for preferred roommates
        Spinner spinnerRoommate = (Spinner) findViewById(R.id.roommate_spinner);
        ArrayAdapter<CharSequence> adapterRoommate = ArrayAdapter.createFromResource(
                this,
                R.array.roommate_array,
                android.R.layout.simple_spinner_item
        );
        adapterRoommate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoommate.setAdapter(adapterRoommate);

        spinnerRoommate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roommate = spinnerRoommate.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //update display name of user
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    //enter user data to Firebase
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(gender, ethnicity, roommate);

                    //extracting user reference from registered users database
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registered successfully. Please verify your email", Toast.LENGTH_SHORT).show();

                                //send verification email
                                firebaseUser.sendEmailVerification();

                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                //prevents user from returning to register activity when pressing back
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Registration failed. Please try again", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

                }
                else{
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        editTextPassword.setError("Your password is too weak");
                        editTextPassword.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editTextEmail.setError("Your email is invalid or already in use");
                        editTextEmail.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e){
                        editTextEmail.setError("User is already registered with this email");
                        editTextEmail.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}