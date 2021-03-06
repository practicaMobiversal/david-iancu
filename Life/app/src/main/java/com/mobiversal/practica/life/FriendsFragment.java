package com.mobiversal.practica.life;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.

 */
public class FriendsFragment extends Fragment {
    private RecyclerView mFriendList;
    private DatabaseReference mFriendsDatabase;
    private FirebaseAuth mAuth;
    private String mCurent_user_id;
    private View mMainView;
    private  DatabaseReference mUserDatabase;
    private  DatabaseReference mUserDatabase1;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);
        mFriendList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();
        mCurent_user_id = mAuth.getCurrentUser().getUid();
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUserDatabase1 = FirebaseDatabase.getInstance().getReference().child("users");
        mUserDatabase.keepSynced(true);
        mUserDatabase1.keepSynced(true);
        mFriendList.setHasFixedSize(true);
        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

   FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                Friends.class,
                R.layout.users_single,
                FriendsViewHolder.class,
                mFriendsDatabase
        ) {



        public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FriendsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
            viewHolder.setOnClickListener(new FriendsViewHolder.ClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    final String user_id = getRef(position).getKey();
                    mUserDatabase1=FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

                    mUserDatabase1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String retrieve_name = dataSnapshot.child("name").getValue(String.class);
                            String retrieve_Email = dataSnapshot.child("Email").getValue(String.class);
                            String retrieve_url = dataSnapshot.child("tumbimg").getValue(String.class);



                            Intent intent = new Intent(getContext(), ChatConversationActivity.class);
                            intent.putExtra("image_id", retrieve_url);
                            intent.putExtra("email", retrieve_Email);
                            intent.putExtra("name", retrieve_name);

                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

            return viewHolder;
        }

            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, Friends friends, int i) {

                viewHolder.mView.setText(friends.getDate());
                final String user_list_id=getRef(i).getKey();
                mUserDatabase.child(user_list_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userName=String.valueOf(dataSnapshot.child("name").getValue());
                        String tumbimg=String.valueOf(dataSnapshot.child("tumbimg").getValue());
                        viewHolder.nameView.setText(userName);
                        Picasso.with(getContext())
                                .load(tumbimg)
                                .placeholder(R.drawable.default_user)
                                .into(viewHolder.circImageView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





            }
        };
        mFriendList.setAdapter(friendsRecyclerViewAdapter);
    }



    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView mView;
        TextView nameView;
        CircleImageView circImageView;

        private FriendsViewHolder.ClickListener mymListener;

        public interface ClickListener {
            public void onItemClick(View view, int position);
        }
        public FriendsViewHolder(View itemView){
            super(itemView);
          mView=(TextView) itemView.findViewById(R.id.user_single_tex);
            nameView = (TextView) itemView.findViewById(R.id.user_single_name);
            circImageView = (CircleImageView) itemView.findViewById(R.id.single_user_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mymListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
        public void setOnClickListener(ClickListener clickListener) {
            mymListener = clickListener;


        }

        }

    }

