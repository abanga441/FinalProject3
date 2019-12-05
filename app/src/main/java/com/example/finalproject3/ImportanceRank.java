package com.example.finalproject3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class ImportanceRank extends AppCompatActivity {

    //declare objects
    TextView textViewHighImportance, textViewBirdName, textViewZipCode, textViewUserEmail, textViewImportanceScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importance_rank);

        //connect to the UI
        textViewHighImportance = findViewById(R.id.textViewHighImportance);
        textViewBirdName = findViewById(R.id.textViewBirdName);
        textViewZipCode = findViewById(R.id.textViewZipCode);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewImportanceScore = findViewById(R.id.textViewImportanceScore);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("birdsightings");



        myRef.orderByChild("birdimportance").limitToLast(1).addChildEventListener(new ChildEventListener() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemBirdInput) {
            Intent InputIntent = new Intent(this, Search.class);
            startActivity(InputIntent);
        } else if (item.getItemId() == R.id.itemSearch) {
            Intent SearchIntent = new Intent(this, Search.class);
            startActivity(SearchIntent);
        } else if (item.getItemId() == R.id.itemLogout) {
            FirebaseAuth.getInstance().signOut(); //actually signing out the user
            Intent LogoutIntent = new Intent(this, MainActivity.class);
            startActivity(LogoutIntent);
        } else if(item.getItemId() == R.id.itemRank) {
            Toast.makeText(this, "You are already here", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
