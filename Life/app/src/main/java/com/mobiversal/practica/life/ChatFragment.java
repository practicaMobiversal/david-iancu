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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatFragment extends Fragment {

  private  TextView person_name,person_email;
   private RecyclerView recyclerView;
  private   FirebaseDatabase firebaseDatabase;
   private DatabaseReference myRef;
    public FirebaseRecyclerAdapter<Show_Chat_Activity_Data_Items, ChatActivity.Show_Chat_ViewHolder> mFirebaseAdapter;
  private   ProgressBar progressBar;
   private LinearLayoutManager mLinearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_chat_layout, container, false);


        firebaseDatabase = FirebaseDatabase.getInstance();

        myRef = firebaseDatabase.getReference("users");
        //myRef.keepSynced(true);


        progressBar = (ProgressBar) view.findViewById(R.id.show_chat_progressBar2);

        //Recycler View
        recyclerView = (RecyclerView)view.findViewById(R.id.show_chat_recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        //mLinearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLinearLayoutManager);

//---------------------------------------------------------------------------------

        progressBar.setVisibility(ProgressBar.VISIBLE);
        //Log.d("LOGGED", "Will Start Calling populateViewHolder : ");
        //Log.d("LOGGED", "IN onStart ");


        mFirebaseAdapter = new FirebaseRecyclerAdapter<Show_Chat_Activity_Data_Items, ChatActivity.Show_Chat_ViewHolder>(Show_Chat_Activity_Data_Items.class, R.layout.show_chat_single_item, ChatActivity.Show_Chat_ViewHolder.class, myRef) {

            public void populateViewHolder(final ChatActivity.Show_Chat_ViewHolder viewHolder, Show_Chat_Activity_Data_Items model, final int position) {
                //Log.d("LOGGED", "populateViewHolder Called: ");
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if (!model.getName().equals("Null")) {
                    viewHolder.Person_Name(model.getName());
                    if(model.gettumbimg() != null)
                        viewHolder.Person_Image(model.gettumbimg());
                    //viewHolder.Person_Email(model.getEmail());
                    if(model.getEmail().equals(LoginActivity.userEmail))
                    {
                        //viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.Layout_hide();

                        //recyclerView.getChildAdapterPosition(viewHolder.itemView.getRootView());
                        // viewHolder.itemView.set;


                    }
                    else
                        viewHolder.Person_Email(model.getEmail());
                }


                //OnClick Item
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {

                        DatabaseReference ref = mFirebaseAdapter.getRef(position);
                        ref.addValueEventListener(new ValueEventListener() {
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
            }
        };

        recyclerView.setAdapter(mFirebaseAdapter);




        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //View Holder For Recycler View
    public static class Show_Chat_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView person_name, person_email;
        private final ImageView person_image;
        private final LinearLayout layout;
        final LinearLayout.LayoutParams params;

        public Show_Chat_ViewHolder(final View itemView) {
            super(itemView);
            person_name = (TextView) itemView.findViewById(R.id.chat_persion_name);
            person_email = (TextView) itemView.findViewById(R.id.chat_persion_email);
            person_image = (ImageView) itemView.findViewById(R.id.chat_persion_image);
            layout = (LinearLayout)itemView.findViewById(R.id.show_chat_single_item_layout);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }


        private void Person_Name(String title) {
            // Log.d("LOGGED", "Setting Name: ");
            person_name.setText(title);
        }
        private void Layout_hide() {
            params.height = 0;
            //itemView.setLayoutParams(params);
            layout.setLayoutParams(params);

        }


        private void Person_Email(String title) {
            person_email.setText(title);
        }


        private void Person_Image(String url) {

            if (!url.equals("Null")) {
                Glide.with(itemView.getContext())
                        .load(url)
                        .crossFade()
                        .thumbnail(0.5f)
//                        .placeholder(R.drawable.loading)
//                        .bitmapTransform(new CircleTransform(itemView.getContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(person_image);
            }

        }


    }


}