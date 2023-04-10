package com.example.androidvotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Profile extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    // Get the currently signed-in user

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("student_info");
    //FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView courseTxt,blockTxt,schoolIdTxt;
    Button editBtn,deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        courseTxt=findViewById(R.id.course);
        blockTxt=findViewById(R.id.block);
        schoolIdTxt=findViewById(R.id.schoolId);


     editBtn=findViewById(R.id.edit);
     deleteBtn=findViewById(R.id.delete);
        if (user != null) {
            String uid = user.getUid();
            ref.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // Get the data from the snapshot
                    if (snapshot.exists()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    String course = (String) data.get("course");
                    String block = (String) data.get("block");
                    String schoolId = (String) data.get("schoolId");

                    courseTxt.setText(course);
                    blockTxt.setText(block);
                    schoolIdTxt.setText(schoolId);
                }  else {
                // Data does not exist, show an error message or create default data

            }
        }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error
                }
            });
        }
     editBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent gotoEdit=new Intent(Profile.this,EditProfile.class);
             startActivity(gotoEdit);
         }
     });

//Condition to get user profile
        if (user != null) {
            // Get the user's email address
            String email = user.getEmail();

            // Get the user's display name
            String displayName = user.getDisplayName();

            // Get the URL of the user's profile picture
            Uri photoUrl = user.getPhotoUrl();

            // Use the email and display name to update the UI, for example:
            TextView emailTextView = findViewById(R.id.email);
            emailTextView.setText(email);
            TextView nameTextView = findViewById(R.id.name);
            nameTextView.setText(displayName);

            // Use the URL to display the profile picture in an ImageView
            ImageView profileImageView = findViewById(R.id.imageView);
            Glide.with(this).load(photoUrl).transform(new CircleCrop()).into(profileImageView);
        }

        //Delete google account
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAccount();
            }
        });

    }

//Delete account
    public void deleteAccount() {
        if (user != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Account!")
                    .setMessage("Are you sure you want to delete your account and all data?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference userRef = database.getReference("student_info").child(user.getUid());
                            userRef.removeValue().addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        mProgressDialog = new ProgressDialog(Profile.this);
                                        mProgressDialog.setMessage("Please Wait");
                                        mProgressDialog.show();
                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent gotoMain= new Intent(Profile.this,MainActivity.class);
                                                    startActivity(gotoMain);
                                                    finish();
                                                    Toast.makeText(getApplicationContext(), "Your account has been deleted.", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "An error occurred while deleting your account.", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "An error occurred while deleting your data.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            Intent gotoMenu= new Intent(Profile.this,Dashboard.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}