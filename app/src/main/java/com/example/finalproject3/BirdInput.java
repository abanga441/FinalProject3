package com.example.finalproject3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BirdInput extends AppCompatActivity implements View.OnClickListener {

    //declare objects
    TextView textViewUserEmail;
    EditText editTextBirdName, editTextZipCode;
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_input);

        //connect to UI
        editTextBirdName = findViewById(R.id.editTextBirdName);
        editTextZipCode = findViewById(R.id.editTextZipCode);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        //make sure the buttons are working
        buttonSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("birdsightings");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            //get text from EditText

            if(editTextBirdName.getText().toString().trim().equalsIgnoreCase("")) {
                editTextBirdName.setError("This cannot be blank. Please add a bird name.");
            } else if(editTextZipCode.getText().toString().trim().equalsIgnoreCase("")) { //this isn't working either
                editTextZipCode.setError("This cannot be blank.  Please add a zip code");
            } else {
                String createBirdName = editTextBirdName.getText().toString();
                String createUserEmail = currentUser.getEmail();
                Integer createZipCode = Integer.parseInt(editTextZipCode.getText().toString());

                try {

                    //(createZipCode = Integer.parseInt(editTextZipCode.getText().toString()));

                } catch (Exception e) {

                    editTextZipCode.setError("Must be a numeric value. Try again");
                }

                //create a new entry of class BirdSightings into the database

                BirdSightings myBirdSightings = new BirdSightings(createBirdName, createUserEmail, createZipCode, 0);

                myRef.push().setValue(myBirdSightings);

                Toast.makeText(this, "Your record has been submitted. Thanks!", Toast.LENGTH_SHORT).show();

            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemBirdInput) {
            Toast.makeText(this, "You are already here", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.itemSearch) {
            Intent SearchIntent = new Intent(this, Search.class);
            startActivity(SearchIntent);
        } else if (item.getItemId() == R.id.itemLogout) {
            Intent LogoutIntent = new Intent(this, MainActivity.class);
            startActivity(LogoutIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
