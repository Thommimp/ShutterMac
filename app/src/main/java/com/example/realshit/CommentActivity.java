package com.example.realshit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realshit.Adapter.CommentAdapter;
import com.example.realshit.Model.Comment;
import com.example.realshit.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    private EditText addComment;
    private ImageView imageProfile;
    private TextView post;

    private String postId;
    private String autherId;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);

        recyclerView.setAdapter(commentAdapter);

        addComment = findViewById(R.id.add_comment);
        imageProfile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        autherId = intent.getStringExtra("autherId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getUserImage();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(addComment.getText().toString())) {
                    Toast.makeText(CommentActivity.this, "No comment added", Toast.LENGTH_SHORT).show();
                }else {
                    putComment();
                    addComment.setText("");
                }
            }
        });

        getComment();
        

    }

    private void getComment() {
        FirebaseFirestore.getInstance().collection("posts").document(postId)
                .collection("comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            Comment comment = ds.toObject(Comment.class);
                            commentList.add(comment);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }
                });


    }

    private void putComment() {

        DocumentReference doc = FirebaseFirestore.getInstance().collection("posts").document(postId)
                .collection("comments").document();
        String commentId = doc.getId();
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", addComment.getText().toString());
        map.put("publisher", firebaseUser.getUid());
        map.put("commenId", commentId);

                doc.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CommentActivity.this, "comment added", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(CommentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void getUserImage() {

        DocumentReference ref = FirebaseFirestore.getInstance().collection("users")
                .document(firebaseUser.getUid());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if (user.getImageUrl().equals("default")){
                    imageProfile.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Picasso.get().load(user.getImageUrl()).into(imageProfile);
                }

            }
        });
    }
}