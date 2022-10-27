package Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realshit.Model.Post;
import com.example.realshit.Model.User;
import com.example.realshit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

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

      // if (post.getDescription().equals(" ")) {
      //     holder.desciption.setVisibility(View.GONE);
      // } else {
      //     holder.desciption.setVisibility(View.VISIBLE);
      //

      // }
        holder.desciption.setText(post.getDescription());
        publisherinfo(holder.username, post.getUser());




     //  FirebaseFirestore.getInstance().collection("Users").document(post.getUser()).collection("posts")
     //          .document(post.getTime()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
     //              @Override
     //              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
     //
     //              }
     //          })


    }

    private void publisherinfo (final TextView username, String userid) {
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

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            more = itemView.findViewById(R.id.img_more);
            username = itemView.findViewById(R.id.txt_username);
            noOfLikes = itemView.findViewById(R.id.likes_text);
            desciption = itemView.findViewById(R.id.txt_description);
        }
    }
}
