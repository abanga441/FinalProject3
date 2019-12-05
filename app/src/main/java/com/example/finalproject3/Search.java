package com.example.finalproject3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Search extends AppCompatActivity implements View.OnClickListener {

    //declare objects
    EditText editTextZipCodeSearch;
    Button buttonSearch, buttonAddImportance;
    TextView textViewBirdName, textViewZipCode, textViewUserEmail, textViewImportanceScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //connect to the UI
        editTextZipCodeSearch = findViewById(R.id.editTextZipCodeSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonAddImportance = findViewById(R.id.buttonAddImportance);
        textViewBirdName = findViewById(R.id.textViewBirdName);
        textViewZipCode = findViewById(R.id.textViewZipCode);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewImportanceScore = findViewById(R.id.textViewImportanceScore);

        buttonSearch.setOnClickListener(this);
        buttonAddImportance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("birdsightings");

        if(v == buttonSearch){
            if(editTextZipCodeSearch.getText().toString().trim().equalsIgnoreCase("")) {
                editTextZipCodeSearch.setError("This cannot be blank. Please enter a Zip Code.");
            } else {
                try {
                    Integer zipTest =Integer.parseInt(editTextZipCodeSearch.getText().toString());
                } catch (Exception e) {
                    editTextZipCodeSearch.setError("This must be a numeric value.");
                    return;

                }
            }


            Integer findZipCode = Integer.parseInt(editTextZipCodeSearch.getText().toString());

            myRef.orderByChild("zipcode").equalTo(findZipCode).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    BirdSightings foundBird = dataSnapshot.getValue(BirdSightings.class);

                    //grab the bird name, the zip code, and the user who put it in
                    String findBirdName = foundBird.birdname;
                    Integer findZipCode = foundBird.zipcode;
                    String findUser = foundBird.useremail;
                    Integer findBirdImportance = foundBird.birdimportance;


                    //diplay the info
                    textViewBirdName.setText(findBirdName);
                    textViewZipCode.setText(findZipCode.toString());
                    textViewUserEmail.setText(findUser);
                    textViewImportanceScore.setText(findBirdImportance.toString());

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    //need to figure out how to add importance to bird score

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if(v == buttonAddImportance) {

            Integer findZipCode = Integer.parseInt(editTextZipCodeSearch.getText().toString());

            myRef.orderByChild("zipcode").equalTo(findZipCode).limitToLast(1).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String editKey = dataSnapshot.getKey();

                    BirdSightings foundBird = dataSnapshot.getValue(BirdSightings.class);

                    Integer editImportance = foundBird.birdimportance + 1;

                    textViewImportanceScore.setText(editImportance.toString());

                    myRef.child(editKey).child("birdimportance").setValue(editImportance);

                    Toast.makeText(Search.this, "Level is set to " + editImportance, Toast.LENGTH_SHORT).show();





                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

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
            Intent InputIntent = new Intent(this, BirdInput.class);
            startActivity(InputIntent);
        } else if (item.getItemId() == R.id.itemSearch) {
            Toast.makeText(this, "You are already here", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.itemLogout) {
            FirebaseAuth.getInstance().signOut(); //actually signing out the user
            Intent LogoutIntent = new Intent(this, MainActivity.class);
            startActivity(LogoutIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
