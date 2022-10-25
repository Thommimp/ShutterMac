package com.example.realshit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class UsernameActivity extends AppCompatActivity {

    private EditText username;
    private Button next;

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        username = findViewById(R.id.edtUsername);
        next = findViewById(R.id.btnNext);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

       next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txtUsername = username.getText().toString().toLowerCase();

                CollectionReference userRef = firestore.collection("users");
                Query query = userRef.whereEqualTo("username", txtUsername);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                documentSnapshot.getString("username");

                                if (task.getResult().size() >= 1) {
                                    Log.d(TAG, "user exist");
                                    Toast.makeText(UsernameActivity.this, "Username already exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                            if (task.getResult().size() == 0) {
                                Log.d(TAG, "username available");
                                Toast.makeText(UsernameActivity.this, "Username available", Toast.LENGTH_SHORT).show();
                                DocumentReference documentReference = firestore.collection("users").document(mAuth.getCurrentUser().getUid());
                                Map<String, Object> map = new HashMap<>();
                                map.put("username", txtUsername);
                                documentReference.set(map, SetOptions.merge());
                                Intent intent = new Intent(UsernameActivity.this, BirthdayActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UsernameActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                }
           });



            }
            }


