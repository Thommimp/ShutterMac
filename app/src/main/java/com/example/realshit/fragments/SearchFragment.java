package com.example.realshit.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.realshit.BirthdayActivity;
import com.example.realshit.R;
import com.example.realshit.UsernameActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class SearchFragment extends Fragment {
    private EditText EditText;
    private Button find;
    private FirebaseFirestore firestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        find = view.findViewById(R.id.btnFind);
        EditText = view.findViewById(R.id.edtText);
        firestore = FirebaseFirestore.getInstance();



        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String txtUsername = EditText.getText().toString();



                CollectionReference userRef = firestore.collection("users");
                Query query = userRef.whereEqualTo("username", txtUsername.toLowerCase());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                documentSnapshot.getString("username");

                                if (task.getResult().size() >= 1) {
                                    Toast.makeText(getActivity(), "Go to users page", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if (task.getResult().size() == 0) {
                            Toast.makeText(getActivity(), "Username does not exist!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;



    }

}