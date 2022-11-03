package com.example.realshit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BirthdayActivity extends AppCompatActivity {
    TextView tvDate;
//    EditText etDate;
    DatePickerDialog.OnDateSetListener setListener;
    Button register;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_birthday);

        tvDate = findViewById(R.id.tv_date);
//        etDate = findViewById(R.id.et_date);
        register = findViewById(R.id.btnRegister);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                     BirthdayActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                             ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                tvDate.setText(date);
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtBirthday = tvDate.getText().toString();
                if (txtBirthday == "") {
                    Toast.makeText(BirthdayActivity.this, "Please enter your birthday", Toast.LENGTH_SHORT).show();
                } else {

                    DocumentReference documentReference = firestore.collection("users").document(mAuth.getCurrentUser().getUid());
                    Map<String, Object> map = new HashMap<>();
                    map.put("birthday", txtBirthday);
                    map.put("bio", "");
                    map.put("imageurl", "default");
                    documentReference.set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(BirthdayActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BirthdayActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
    }
}