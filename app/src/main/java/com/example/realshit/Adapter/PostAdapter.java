package com.example.realshit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realshit.CommentActivity;
import com.example.realshit.Model.Post;
import com.example.realshit.Model.User;
import com.example.realshit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder> {

    private Context mContext;
    private List<Post> mPosts;


    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        final Post post = mPosts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.post_image);

        if (post.getDescription().equals("")) {
            holder.desciption.setVisibility(View.GONE);
        } else {
            holder.desciption.setVisibility(View.VISIBLE);
            holder.desciption.setText(post.getDescription());

        }

        publisherinfo(holder.username, post.getUser());
        isLiked(post.getPostid(), holder.like);
        noOfLikes(post.getPostid(), holder.noOfLikes);
        getComments(post.getPostid(), holder.noOfComments);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")) {


                    DocumentReference doc = FirebaseFirestore.getInstance().collection("posts").document(post.getPostid())
                            .collection("likes").document(firebaseUser.getUid());
                    Map<String, Object> map = new HashMap<>();
                    map.put(firebaseUser.getUid(), "true");
                    doc.set(map);
                    Toast.makeText(mContext, "liked", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentReference doc = FirebaseFirestore.getInstance().collection("posts").document(post.getPostid())
                            .collection("likes").document(firebaseUser.getUid());
                    doc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(mContext, "document delted", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }

        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("autherId", post.getUser());
                mContext.startActivity(intent);

            }
        });

        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("autherId", post.getUser());
                mContext.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public ImageView post_image;
        public ImageView like;
        public ImageView comment;
        public ImageView more;

        public TextView username;
        public TextView noOfLikes;

        public TextView desciption;
        public TextView noOfComments;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            more = itemView.findViewById(R.id.img_more);
            username = itemView.findViewById(R.id.txt_username);
            noOfLikes = itemView.findViewById(R.id.likes_text);
            desciption = itemView.findViewById(R.id.txt_description);
            noOfComments = itemView.findViewById(R.id.comments_text);
        }
    }

    private void publisherinfo(final TextView username, String userid) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(userid);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                username.setText(user.getUsername());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void isLiked(String postid, final ImageView imageView) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference reference = FirebaseFirestore.getInstance().collection("posts").document(postid)
                .collection("likes").document(firebaseUser.getUid());

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }


            }
        });

    }

    private void noOfLikes(String postId, TextView text) {
        CollectionReference ref = FirebaseFirestore.getInstance().collection("posts")
                .document(postId).collection("likes");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().size() != 0) {
                    text.setText(String.valueOf(task.getResult().size()));
                } else {
                    text.setText("");
                }


            }
        });


    }

    private void getComments(String postId, TextView text) {
        CollectionReference ref = FirebaseFirestore.getInstance().collection("posts")
                .document(postId).collection("comments");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().size() != 0) {
                    text.setText(String.valueOf(task.getResult().size()));
                } else {
                    text.setText("");
                }


            }
        });

    }
}