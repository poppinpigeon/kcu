package com.cs407.badgerroomie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RoommateProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommate_profile);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String gender = getIntent().getStringExtra("gender");
        String ethnicity = getIntent().getStringExtra("ethnicity");
        String roommate = getIntent().getStringExtra("roommate");

        TextView textViewName = findViewById(R.id.textView_name_detail);
        TextView textViewEmail = findViewById(R.id.textView_email_detail);
        TextView textViewGender = findViewById(R.id.textView_gender_detail);
        TextView textViewEthnicity = findViewById(R.id.textView_ethnicity_detail);
        TextView textViewRoommate = findViewById(R.id.textView_roommate_detail);

        textViewName.setText(name);
        textViewEmail.setText(email);
        textViewGender.setText(gender);
        textViewEthnicity.setText(ethnicity);
        textViewRoommate.setText(roommate);
    }
}