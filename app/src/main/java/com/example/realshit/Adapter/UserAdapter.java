package com.example.realshit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realshit.Model.User;
import com.example.realshit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean isFargment;

    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    public UserAdapter(Context mContext, List<User> mUsers, boolean isFargment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFargment = isFargment;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        User user = mUsers.get(position);
        holder.btnFollow.setVisibility(View.VISIBLE);

        holder.username.setText(user.getUsername());
        holder.bio.setText(user.getBio());

        Picasso.get().load(user.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);

        isFollowed(user.getId(), holder.btnFollow);

        if (user.getId().equals(firebaseUser.getUid())){
            holder.btnFollow.setVisibility(View.GONE);
        }

    }

    private void isFollowed(String id, Button btnFollow) {

        CollectionReference reference = firestore.collection("users");
        CollectionReference refenrece2 = reference.document(mAuth.getUid()).collection("following");
        Query query = refenrece2.whereEqualTo("following", firebaseUser.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        documentSnapshot.getString("following");
                    }
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public CircleImageView imageProfile;
        public TextView username;
        public TextView bio;
        public Button btnFollow;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.edtUsername);
            bio = itemView.findViewById(R.id.bio);
            btnFollow = itemView.findViewById(R.id.btn_follow);
            mAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();
        }
    }
}
