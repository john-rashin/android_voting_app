package com.example.androidvotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Createpool extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("pool_info");

    EditText nameTxt,passwordTxt;
    TextView startView,endView,idView,passView,successView;
    Button saveBtn;
    View layout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpool);

        LayoutInflater Inflater = getLayoutInflater();
         layout = Inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_layout));

        ImageView image = (ImageView) layout.findViewById(R.id.image_view);
        image.setImageResource(R.drawable.img);

        nameTxt = findViewById(R.id.name);
        passwordTxt = findViewById(R.id.password);
        saveBtn = findViewById(R.id.saveBTN);
        idView = findViewById(R.id.id_view);
        passView = findViewById(R.id.password_view);
        successView = findViewById(R.id.success_view);
//Start voting Date
        startView = findViewById(R.id.startCalendar);
        startView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a DatePickerDialog and a TimePickerDialog
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Createpool.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Do something with the selected date
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(Createpool.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Do something with the selected time
                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                String dateTime = sdf.format(calendar.getTime());
                                startView.setText(dateTime);
                            }
                        }, hour, minute, false);

                        timePickerDialog.show();
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

//End voting Date
        endView = findViewById(R.id.endCalendar);
        endView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a DatePickerDialog and a TimePickerDialog
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Createpool.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Do something with the selected date
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(Createpool.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Do something with the selected time
                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                String dateTime = sdf.format(calendar.getTime());
                                endView.setText(dateTime);
                            }
                        }, hour, minute, false);

                        timePickerDialog.show();
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePool();
                DatabaseReference reff = FirebaseDatabase.getInstance().getReference("pool_info");
                String uid = user.getUid();
                reff.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get the ID and password
                        String id = dataSnapshot.getKey();
                        String password = dataSnapshot.child("Password").getValue(String.class);

                        // Display the ID and password in the TextView

                        idView.setText("Key: " + id);
                        passView.setText("Password: " + password);

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                        ClipData clip = ClipData.newPlainText("ID", id);

                        clipboard.setPrimaryClip(clip);

                        // Display a success message in the TextView

                        successView.setText("Created successfully!");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
            }
        });


}
        //function for saving pool
        public void savePool() {

            String name = nameTxt.getText().toString();
            String pass = passwordTxt.getText().toString();
            String startDate = startView.getText().toString();
            String endDate = endView.getText().toString();


            if (user != null) {
                String uid = user.getUid();
                Map<String, Object> updates = new HashMap<>();
                updates.put("Pool name", name);
                updates.put("Password", pass);
                updates.put("Start Date", startDate);
                updates.put("End Date", endDate);

                nameTxt.setText(" ");
                passwordTxt.setText(" ");

                ref.child(uid).updateChildren(updates);

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            } else {
                Toast.makeText(this, "Error to Saved", Toast.LENGTH_SHORT).show();
            }

        }

    }
