package com.example.androidvotingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class EditProfile extends AppCompatActivity {

    EditText courseTxt,blockTxt,schoolIdTxt;
    Button saveBtn;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("student_info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseTxt=findViewById(R.id.course);
        blockTxt=findViewById(R.id.block);
        schoolIdTxt=findViewById(R.id.schoolId);

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
                        Toast.makeText(EditProfile.this, "Fill up all field", Toast.LENGTH_SHORT).show();
                }
            }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error

                }
            });
        }

        //Save data to database
        saveBtn=findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editProfile();
            }
        });
    }
    public void editProfile() {

    String course= courseTxt.getText().toString();
      String block=  blockTxt.getText().toString();
       String schoolId= schoolIdTxt.getText().toString();


        if (user != null) {
            String uid = user.getUid();
            Map<String, Object> updates = new HashMap<>();
            updates.put("course", course);
            updates.put("block", block);
            updates.put("schoolId", schoolId);
            ref.child(uid).updateChildren(updates);
            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Error to Saved", Toast.LENGTH_SHORT).show();
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
            Intent gotoMenu= new Intent(EditProfile.this,Profile.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}