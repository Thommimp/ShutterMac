package com.example.realshit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.realshit.Model.Post;
import com.example.realshit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import com.example.realshit.Adapter.PostAdapter;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private List<String> followingList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerViewPosts.setAdapter(postAdapter);


        checkFollowingUsers();
        return view;

    }

    private void checkFollowingUsers() {
        followingList = new ArrayList<>();
        CollectionReference reference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("following");
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                followingList.clear();
                for (DocumentSnapshot ds: task.getResult()) {
                    followingList.add(ds.getString("followid"));
                }
                readPosts();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void readPosts() {
        Task<QuerySnapshot> ref = FirebaseFirestore.getInstance().collection("posts").orderBy("time").get();
                ref.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        postList.clear();
                        for (DocumentSnapshot ds: task.getResult()){
                            Post post = ds.toObject(Post.class);

                            for (String id : followingList) {
                                if (post.getUser().equals(id)) {
                                    postList.add(post);
                                }

                            }
                        }
                        postAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "shakalaka", Toast.LENGTH_SHORT).show();
                    }
                });
    }

  // private void order() {
  //     CollectionReference ref = FirebaseFirestore.getInstance().collection("posts");

  //     Query query = ref.orderBy("time");
  //     query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
  //         @Override
  //         public void onComplete(@NonNull Task<QuerySnapshot> task) {
  //             for (DocumentSnapshot ds: task.getResult()) {
  //                 postList.add(ds.getString("time"));
  //             }
  //
  //         }
  //     });
  // }


}